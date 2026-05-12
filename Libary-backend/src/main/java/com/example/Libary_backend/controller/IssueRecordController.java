package com.example.Libary_backend.controller;

import com.example.Libary_backend.dto.request.IssueRecordRequestDTO;
import com.example.Libary_backend.dto.request.ReturnBookRequestDTO;
import com.example.Libary_backend.dto.response.IssueRecordResponseDTO;
import com.example.Libary_backend.service.IssueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/issue-records")
@CrossOrigin(origins = "*", maxAge = 3600)
public class IssueRecordController {

    @Autowired
    private IssueRecordService issueRecordService;

    /**
     * Issue a book to a member
     * POST /api/issue-records/issue
     */
    @PostMapping("/issue")
    public ResponseEntity<IssueRecordResponseDTO> issueBook(
            @RequestBody IssueRecordRequestDTO requestDTO) {
        IssueRecordResponseDTO response = issueRecordService.issueBook(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Return a book to the library
     * PUT /api/issue-records/return
     */
    @PutMapping("/return")
    public ResponseEntity<IssueRecordResponseDTO> returnBook(
            @RequestBody ReturnBookRequestDTO requestDTO) {
        IssueRecordResponseDTO response = issueRecordService.returnBook(requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Get issue record by ID
     * GET /api/issue-records/{issueId}
     */
    @GetMapping("/{issueId}")
    public ResponseEntity<IssueRecordResponseDTO> getIssueRecordById(
            @PathVariable Long issueId) {
        IssueRecordResponseDTO response = issueRecordService.getIssueRecordById(issueId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all issue records for a member
     * GET /api/issue-records/member/{memberId}
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<IssueRecordResponseDTO>> getIssueRecordsByMemberId(
            @PathVariable Long memberId) {
        List<IssueRecordResponseDTO> response = 
                issueRecordService.getIssueRecordsByMemberId(memberId);
        return ResponseEntity.ok(response);
    }
}
