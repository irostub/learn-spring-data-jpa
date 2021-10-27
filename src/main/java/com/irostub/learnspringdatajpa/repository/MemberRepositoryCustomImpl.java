package com.irostub.learnspringdatajpa.repository;

import com.irostub.learnspringdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{
    private final EntityManager em;

    @Override
    public Optional<Member> findCustomByUsername(String username) {
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getSingleResult();
        return Optional.ofNullable(findMember);
    }
}
