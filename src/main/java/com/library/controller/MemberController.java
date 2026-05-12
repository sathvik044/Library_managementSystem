package com.library.controller;

import com.library.dto.ApiResponse;
import com.library.dto.IssueResponse;
import com.library.model.Member;
import com.library.service.IssueService;
import com.library.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final IssueService issueService;

    public MemberController(MemberService memberService, IssueService issueService) {
        this.memberService = memberService;
        this.issueService = issueService;
    }

    /**
     * POST /members - Register a new member.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Member>> registerMember(@Valid @RequestBody Member member) {
        Member saved = memberService.registerMember(member);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Member registered successfully", saved));
    }

    /**
     * GET /members - View all members.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Member>>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(
                ApiResponse.success("Retrieved all members (" + members.size() + ")", members));
    }

    /**
     * GET /members/{memberId} - View member details.
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Member>> getMemberById(@PathVariable Long memberId) {
        Member member = memberService.getMemberById(memberId);
        return ResponseEntity.ok(ApiResponse.success("Member retrieved", member));
    }

    /**
     * GET /members/{memberId}/issues - View books issued to a member.
     */
    @GetMapping("/{memberId}/issues")
    public ResponseEntity<ApiResponse<List<IssueResponse>>> getMemberIssues(@PathVariable Long memberId) {
        List<IssueResponse> issues = issueService.getIssuesByMember(memberId);
        return ResponseEntity.ok(
                ApiResponse.success("Retrieved issues for member ID " + memberId + " (" + issues.size() + ")", issues));
    }
}
