package com.irostub.learnspringdatajpa.repository;

import com.irostub.learnspringdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberJpaRepository {
    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public List<Member> findByName(String username) {
        return em.createQuery("select m from Member m where m.username like concat('%',:username,'%')", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m where m.username=:username and m.age>:age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age>=:age order by m.username asc", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Member> findAllWithTeam() {
        return em.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age>:age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }
}
