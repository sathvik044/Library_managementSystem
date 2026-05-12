package com.library.dto;

import java.time.LocalDate;

public class IssueResponse {

    private Long issueId;
    private Long bookId;
    private String bookTitle;
    private Long memberId;
    private String memberName;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private boolean active;

    public IssueResponse() {}

    public IssueResponse(Long issueId, Long bookId, String bookTitle, Long memberId,
                         String memberName, LocalDate issueDate, LocalDate returnDate, boolean active) {
        this.issueId = issueId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.memberId = memberId;
        this.memberName = memberName;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.active = active;
    }

    public Long getIssueId() { return issueId; }
    public void setIssueId(Long issueId) { this.issueId = issueId; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
