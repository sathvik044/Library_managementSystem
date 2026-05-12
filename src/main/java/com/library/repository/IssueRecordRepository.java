package com.library.repository;

import com.library.model.IssueRecord;
import com.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {

    /**
     * Find all active issues for a member (books not yet returned).
     */
    List<IssueRecord> findByMemberAndReturnDateIsNull(Member member);

    /**
     * Count active issues for a member.
     */
    long countByMemberAndReturnDateIsNull(Member member);

    /**
     * Find active issue for a specific book (should be at most one).
     */
    Optional<IssueRecord> findByBook_BookIdAndReturnDateIsNull(Long bookId);

    /**
     * Find all issues for a member (both active and returned).
     */
    List<IssueRecord> findByMember_MemberId(Long memberId);

    /**
     * Find all active issues.
     */
    List<IssueRecord> findByReturnDateIsNull();
}
