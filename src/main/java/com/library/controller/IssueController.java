package com.library.controller;

import com.library.dto.ApiResponse;
import com.library.dto.IssueRequest;
import com.library.dto.IssueResponse;
import com.library.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    /**
     * POST /issues/issue - Issue a book to a member.
     */
    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<IssueResponse>> issueBook(@Valid @RequestBody IssueRequest request) {
        IssueResponse response = issueService.issueBook(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Book '" + response.getBookTitle() + "' issued to '" + response.getMemberName() + "' successfully",
                        response));
    }

    /**
     * PUT /issues/return/{issueId} - Return an issued book.
     */
    @PutMapping("/return/{issueId}")
    public ResponseEntity<ApiResponse<IssueResponse>> returnBook(@PathVariable Long issueId) {
        IssueResponse response = issueService.returnBook(issueId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Book '" + response.getBookTitle() + "' returned by '" + response.getMemberName() + "' successfully",
                        response));
    }

    /**
     * GET /issues - View all issue records.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<IssueResponse>>> getAllIssues() {
        List<IssueResponse> issues = issueService.getAllIssues();
        return ResponseEntity.ok(
                ApiResponse.success("Retrieved all issue records (" + issues.size() + ")", issues));
    }

    /**
     * GET /issues/active - View all active (unreturned) issues.
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<IssueResponse>>> getActiveIssues() {
        List<IssueResponse> issues = issueService.getActiveIssues();
        return ResponseEntity.ok(
                ApiResponse.success("Retrieved active issue records (" + issues.size() + ")", issues));
    }
}
