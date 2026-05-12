package com.example.Libary_backend.service;

import com.example.Libary_backend.dto.request.MemberRequestDTO;
import com.example.Libary_backend.dto.response.MemberResponseDTO;

import java.util.List;

public interface MemberService {
    MemberResponseDTO registerMember(MemberRequestDTO requestDTO);
    MemberResponseDTO getMemberById(Long memberId);
    List<MemberResponseDTO> getAllMembers();
}