package com.example.Libary_backend.service;

import com.example.Libary_backend.dto.request.IssueRecordRequestDTO;
import com.example.Libary_backend.dto.request.ReturnBookRequestDTO;
import com.example.Libary_backend.dto.response.IssueRecordResponseDTO;
import java.util.List;

public interface IssueRecordService {
    
    IssueRecordResponseDTO issueBook(IssueRecordRequestDTO requestDTO);
    
    IssueRecordResponseDTO returnBook(ReturnBookRequestDTO requestDTO);
    
    IssueRecordResponseDTO getIssueRecordById(Long issueId);
    
    List<IssueRecordResponseDTO> getIssueRecordsByMemberId(Long memberId);

    List<IssueRecordResponseDTO> getAllIssueRecords();

    List<IssueRecordResponseDTO> getActiveIssueRecords();
}
