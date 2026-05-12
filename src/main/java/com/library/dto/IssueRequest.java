package com.library.dto;

import jakarta.validation.constraints.NotNull;

public class IssueRequest {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Member ID is required")
    private Long memberId;

    public IssueRequest() {}

    public IssueRequest(Long bookId, Long memberId) {
        this.bookId = bookId;
        this.memberId = memberId;
    }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
}
