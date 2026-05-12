package com.library.service;

import com.library.dto.IssueRequest;
import com.library.dto.IssueResponse;
import com.library.exception.BusinessRuleException;
import com.library.exception.ResourceNotFoundException;
import com.library.model.Book;
import com.library.model.IssueRecord;
import com.library.model.Member;
import com.library.repository.IssueRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IssueService {

    private static final int MAX_ACTIVE_ISSUES = 3;

    private final IssueRecordRepository issueRecordRepository;
    private final BookService bookService;
    private final MemberService memberService;

    public IssueService(IssueRecordRepository issueRecordRepository,
                        BookService bookService,
                        MemberService memberService) {
        this.issueRecordRepository = issueRecordRepository;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    /**
     * Issue a book to a member.
     * Business Rules:
     * - Book must be available
     * - Member can have max 3 active issues
     */
    public IssueResponse issueBook(IssueRequest request) {
        Book book = bookService.getBookById(request.getBookId());
        Member member = memberService.getMemberById(request.getMemberId());

        // Rule: Book must be available
        if (!book.isAvailability()) {
            throw new BusinessRuleException(
                    "Book '" + book.getTitle() + "' is currently not available. It is already issued to another member.");
        }

        // Rule: Member can have max 3 active issues
        long activeIssues = issueRecordRepository.countByMemberAndReturnDateIsNull(member);
        if (activeIssues >= MAX_ACTIVE_ISSUES) {
            throw new BusinessRuleException(
                    "Member '" + member.getName() + "' already has " + activeIssues +
                    " active book issues. Maximum allowed is " + MAX_ACTIVE_ISSUES + ".");
        }

        // Create issue record
        IssueRecord issueRecord = new IssueRecord();
        issueRecord.setBook(book);
        issueRecord.setMember(member);
        issueRecord.setIssueDate(LocalDate.now());
        issueRecord.setReturnDate(null);

        issueRecordRepository.save(issueRecord);

        // Mark book as unavailable
        bookService.updateAvailability(book.getBookId(), false);

        return mapToResponse(issueRecord);
    }

    /**
     * Return a book.
     * - Sets return date to today
     * - Marks book as available
     */
    public IssueResponse returnBook(Long issueId) {
        IssueRecord issueRecord = issueRecordRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue record not found with ID: " + issueId));

        if (issueRecord.getReturnDate() != null) {
            throw new BusinessRuleException(
                    "This book has already been returned on " + issueRecord.getReturnDate());
        }

        // Update return date
        issueRecord.setReturnDate(LocalDate.now());
        issueRecordRepository.save(issueRecord);

        // Mark book as available again
        bookService.updateAvailability(issueRecord.getBook().getBookId(), true);

        return mapToResponse(issueRecord);
    }

    /**
     * Get all issues (active and returned).
     */
    @Transactional(readOnly = true)
    public List<IssueResponse> getAllIssues() {
        return issueRecordRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all active issues (books not yet returned).
     */
    @Transactional(readOnly = true)
    public List<IssueResponse> getActiveIssues() {
        return issueRecordRepository.findByReturnDateIsNull().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all issues for a specific member.
     */
    @Transactional(readOnly = true)
    public List<IssueResponse> getIssuesByMember(Long memberId) {
        // Validate member exists
        memberService.getMemberById(memberId);
        return issueRecordRepository.findByMember_MemberId(memberId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Map IssueRecord entity to IssueResponse DTO.
     */
    private IssueResponse mapToResponse(IssueRecord record) {
        IssueResponse response = new IssueResponse();
        response.setIssueId(record.getIssueId());
        response.setBookId(record.getBook().getBookId());
        response.setBookTitle(record.getBook().getTitle());
        response.setMemberId(record.getMember().getMemberId());
        response.setMemberName(record.getMember().getName());
        response.setIssueDate(record.getIssueDate());
        response.setReturnDate(record.getReturnDate());
        response.setActive(record.isActive());
        return response;
    }
}
