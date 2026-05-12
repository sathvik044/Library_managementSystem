package com.example.Libary_backend.mapper;

import com.example.Libary_backend.dto.response.IssueRecordResponseDTO;
import com.example.Libary_backend.entity.IssueRecord;
import org.springframework.stereotype.Component;

@Component
public class IssueRecordMapper {
    
    public IssueRecordResponseDTO toDTO(IssueRecord issueRecord) {
        if (issueRecord == null) {
            return null;
        }

        return IssueRecordResponseDTO.builder()
                .issueId(issueRecord.getIssueId())
                .bookId(issueRecord.getBook().getBookId())
                .bookTitle(issueRecord.getBook().getTitle())
                .bookAuthor(issueRecord.getBook().getAuthor())
                .memberId(issueRecord.getMember().getMemberId())
                .memberName(issueRecord.getMember().getName())
                .memberEmail(issueRecord.getMember().getEmail())
                .issueDate(issueRecord.getIssueDate())
                .returnDate(issueRecord.getReturnDate())
                .build();
    }

    public IssueRecord toEntity(IssueRecordResponseDTO dto) {
        if (dto == null) {
            return null;
        }

        return IssueRecord.builder()
                .issueId(dto.getIssueId())
                .issueDate(dto.getIssueDate())
                .returnDate(dto.getReturnDate())
                .build();
    }
}
