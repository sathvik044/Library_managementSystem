package com.example.Libary_backend.service.serviceImpl;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Libary_backend.dto.request.MemberRequestDTO;
import com.example.Libary_backend.dto.response.MemberResponseDTO;
import com.example.Libary_backend.entity.Member;
import com.example.Libary_backend.exception.DuplicateEmailException;
import com.example.Libary_backend.exception.MemberNotFoundException;
import com.example.Libary_backend.mapper.MemberMapper;
import com.example.Libary_backend.repository.MemberRepository;
import com.example.Libary_backend.service.MemberService;
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

   @Override
public MemberResponseDTO registerMember(MemberRequestDTO requestDTO) {

    if(memberRepository.findByEmail(requestDTO.getEmail()).isPresent()) {

        throw new DuplicateEmailException("Email already registered");
    }

    Member member = MemberMapper.toEntity(requestDTO);

    Member savedMember = memberRepository.save(member);

    return MemberMapper.toResponseDTO(savedMember);
}
   @Override
public MemberResponseDTO getMemberById(Long memberId) {

    Member member = memberRepository.findById(memberId)
            .orElseThrow(() ->
                    new MemberNotFoundException("Member not found with ID: " + memberId));

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