package com.example.Libary_backend.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueRecordResponseDTO {
    
    private Long issueId;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private Long memberId;
    private String memberName;
    private String memberEmail;
    private LocalDate issueDate;
    private LocalDate returnDate;
}