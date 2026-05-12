package com.library.repository;

import com.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Check if a member with the given email already exists.
     */
    boolean existsByEmail(String email);

    /**
     * Find a member by email.
     */
    Optional<Member> findByEmail(String email);
}
