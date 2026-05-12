package com.example.Libary_backend.controller;

import com.example.Libary_backend.dto.request.MemberRequestDTO;
import com.example.Libary_backend.dto.response.MemberResponseDTO;
import com.example.Libary_backend.dto.response.IssueRecordResponseDTO;
import com.example.Libary_backend.service.MemberService;
import com.example.Libary_backend.service.IssueRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private IssueRecordService issueRecordService;

    @PostMapping
    public ResponseEntity<MemberResponseDTO> registerMember(
            @Valid @RequestBody MemberRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.registerMember(requestDTO));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> getMemberById(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMemberById(memberId));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{memberId}/books")
    public ResponseEntity<List<IssueRecordResponseDTO>> getIssuedBooksForMember(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(issueRecordService.getIssueRecordsByMemberId(memberId));
    }
}