package com.example.Libary_backend.mapper;
import com.example.Libary_backend.dto.request.MemberRequestDTO;
import com.example.Libary_backend.dto.response.MemberResponseDTO;
import com.example.Libary_backend.entity.Member;

public class MemberMapper {

    public static Member toEntity(MemberRequestDTO dto) {

    return Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static MemberResponseDTO toResponseDTO(Member member) {
        return MemberResponseDTO.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}