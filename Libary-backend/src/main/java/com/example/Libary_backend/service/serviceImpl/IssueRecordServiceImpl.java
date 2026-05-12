package com.example.Libary_backend.service.serviceImpl;

import com.example.Libary_backend.dto.request.IssueRecordRequestDTO;
import com.example.Libary_backend.dto.request.ReturnBookRequestDTO;
import com.example.Libary_backend.dto.response.IssueRecordResponseDTO;
import com.example.Libary_backend.entity.Book;
import com.example.Libary_backend.entity.IssueRecord;
import com.example.Libary_backend.entity.Member;
import com.example.Libary_backend.exception.BookAlreadyIssuedException;
import com.example.Libary_backend.exception.BookNotFoundException;
import com.example.Libary_backend.exception.IssueRecordNotFoundException;
import com.example.Libary_backend.exception.MemberMaxBooksExceededException;
import com.example.Libary_backend.exception.MemberNotFoundException;
import com.example.Libary_backend.mapper.IssueRecordMapper;
import com.example.Libary_backend.repository.BookRepository;
import com.example.Libary_backend.repository.IssueRecordRepository;
import com.example.Libary_backend.repository.MemberRepository;
import com.example.Libary_backend.service.IssueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueRecordServiceImpl implements IssueRecordService {

    @Autowired
    private IssueRecordRepository issueRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private IssueRecordMapper issueRecordMapper;

    /**
     * Issue a book to a member with business rule validations
     * Rule 1: Book must be available
     * Rule 2: Member cannot have more than 3 active books
     * Rule 3: Mark book as unavailable after issue
     */
    @Override
    public IssueRecordResponseDTO issueBook(IssueRecordRequestDTO requestDTO) {
        
        // Fetch book and member
        Book book = bookRepository.findById(requestDTO.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        
        Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        // Rule 1: Check if book is available
        if (!book.getAvailability()) {
            throw new BookAlreadyIssuedException("Book already issued");
        }

        // Rule 2: Check max 3 books per member
        long activeBooks = issueRecordRepository.countByMemberMemberIdAndReturnDateIsNull(
                member.getMemberId());
        
        if (activeBooks >= 3) {
            throw new MemberMaxBooksExceededException("Maximum 3 books allowed");
        }

        // Create new issue record
        IssueRecord issueRecord = IssueRecord.builder()
                .book(book)
                .member(member)
                .issueDate(LocalDate.now())
                .returnDate(null)
                .build();

        // Rule 3: Mark book unavailable after issue
        book.setAvailability(false);
        bookRepository.save(book);

        // Save the issue record
        IssueRecord savedIssueRecord = issueRecordRepository.save(issueRecord);

        return issueRecordMapper.toDTO(savedIssueRecord);
    }

    /**
     * Return a book to the library
     * Rule 4: Set return date to current date and mark book as available
     */
    @Override
    public IssueRecordResponseDTO returnBook(ReturnBookRequestDTO requestDTO) {
        
        IssueRecord issue = issueRecordRepository.findById(requestDTO.getIssueId())
                .orElseThrow(() -> new IssueRecordNotFoundException("Issue record not found"));

        // Rule 4: Return book - set return date and mark available
        issue.setReturnDate(LocalDate.now());

        Book book = issue.getBook();
        book.setAvailability(true);
        bookRepository.save(book);

        IssueRecord updatedIssueRecord = issueRecordRepository.save(issue);

        return issueRecordMapper.toDTO(updatedIssueRecord);
    }

    @Override
    public IssueRecordResponseDTO getIssueRecordById(Long issueId) {
        IssueRecord issueRecord = issueRecordRepository.findById(issueId)
                .orElseThrow(() -> new IssueRecordNotFoundException("Issue record not found"));
        return issueRecordMapper.toDTO(issueRecord);
    }

    @Override
    public List<IssueRecordResponseDTO> getIssueRecordsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
        
        List<IssueRecord> issueRecords = issueRecordRepository.findByMember(member);
        
        return issueRecords.stream()
                .map(issueRecordMapper::toDTO)
                .collect(Collectors.toList());
    }
}
