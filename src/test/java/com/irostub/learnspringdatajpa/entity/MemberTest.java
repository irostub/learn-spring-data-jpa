package com.irostub.learnspringdatajpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberTest {
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("회원 엔티티 테스트")
    @Transactional
    void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("irostub", 10);
        Member member2 = new Member("iro", 11);
        Member member3 = new Member("stub", 12);
        Member member4 = new Member("stub iro", 13);

        member1.changeTeam(teamA);
        member2.changeTeam(teamA);
        member3.changeTeam(teamB);
        member4.changeTeam(teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member=" + member);
            System.out.println("-> member.team=" + member.getTeam());
        }
    }

    @Test
    @DisplayName("등록일 테스트")
    @Transactional
    void testCreatedDate() {
        Member member = new Member("stub", 10);
        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());
        assertNotNull(findMember.getCreatedDate());
    }

    @Test
    @DisplayName("수정일 테스트")
    @Transactional
    void testUpdatedDate() throws Exception {
        Member member = new Member("stub", 10);
        em.persist(member);

        em.flush();
        em.clear();
        sleep(500);

        Member findMember = em.find(Member.class, member.getId());
        Team team = new Team("TeamA");
        em.persist(team);
        findMember.changeTeam(team);

        em.flush();
        em.clear();

        Member validMember = em.find(Member.class, member.getId());

        assertNotNull(validMember.getModifiedDate());
        assertTrue(validMember.getCreatedDate().isBefore(validMember.getModifiedDate()));
    }

    @Test
    @Transactional
    @DisplayName("등록자 테스트")
    void testCreatedBy() {
        Member member = new Member("stub", 10);
        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());
        assertNotNull(findMember.getCreatedBy());
    }

    @Test
    @Transactional
    @DisplayName("수정자 테스트")
    void testUpdatedBy() throws Exception {
        Member member = new Member("stub", 10);
        em.persist(member);

        em.flush();
        em.clear();
        sleep(500);

        Member findMember = em.find(Member.class, member.getId());
        Team team = new Team("TeamA");
        em.persist(team);
        findMember.changeTeam(team);

        em.flush();
        em.clear();

        Member validMember = em.find(Member.class, member.getId());
        assertNotNull(validMember.getModifiedBy());
    }
}