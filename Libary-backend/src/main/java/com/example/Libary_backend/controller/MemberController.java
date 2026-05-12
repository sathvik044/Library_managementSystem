package com.example.Libary_backend.controller;

import com.example.Libary_backend.dto.request.MemberRequestDTO;
import com.example.Libary_backend.dto.response.MemberResponseDTO;
import com.example.Libary_backend.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;
    @PostMapping
    public MemberResponseDTO registerMember(@RequestBody MemberRequestDTO requestDTO) {

        return memberService.registerMember(requestDTO);
    }
    @GetMapping("/{memberId}")
    public MemberResponseDTO getMemberById(@PathVariable Long memberId) {

        return memberService.getMemberById(memberId);
    }
    @GetMapping
    public List<MemberResponseDTO> getAllMembers() {

        return memberService.getAllMembers();
    }
}