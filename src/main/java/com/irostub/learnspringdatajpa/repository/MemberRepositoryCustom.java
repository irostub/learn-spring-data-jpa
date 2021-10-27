package com.irostub.learnspringdatajpa.repository;

import com.irostub.learnspringdatajpa.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findCustomByUsername(String username);
}
