package com.example.Libary_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.Libary_backend.entity.Member;

import com.example.Libary_backend.entity.IssueRecord;

@Repository
public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {

    List<IssueRecord> findByMember(Member member);

    long countByMemberMemberIdAndReturnDateIsNull(Long memberId);

    List<IssueRecord> findByReturnDateIsNull();
}
