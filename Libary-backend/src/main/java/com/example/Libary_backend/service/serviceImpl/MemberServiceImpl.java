package com.example.Libary_backend.service.serviceImpl;

import com.example.Libary_backend.dto.request.MemberRequestDTO;
import com.example.Libary_backend.dto.response.MemberResponseDTO;
import com.example.Libary_backend.entity.Member;
import com.example.Libary_backend.mapper.MemberMapper;
import com.example.Libary_backend.repository.MemberRepository;
import com.example.Libary_backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public MemberResponseDTO registerMember(MemberRequestDTO requestDTO) {

        Member member = MemberMapper.toEntity(requestDTO);

        Member savedMember = memberRepository.save(member);

        return MemberMapper.toResponseDTO(savedMember);
    }
    @Override
    public MemberResponseDTO getMemberById(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return MemberMapper.toResponseDTO(member);
    }
    @Override
    public List<MemberResponseDTO> getAllMembers() {

        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}