package com.library.service;

import com.library.exception.BusinessRuleException;
import com.library.exception.ResourceNotFoundException;
import com.library.model.Member;
import com.library.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Register a new member.
     */
    public Member registerMember(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new BusinessRuleException("A member with email '" + member.getEmail() + "' already exists");
        }
        return memberRepository.save(member);
    }

    /**
     * Get all members.
     */
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    /**
     * Get a member by ID.
     */
    @Transactional(readOnly = true)
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId));
    }
}
