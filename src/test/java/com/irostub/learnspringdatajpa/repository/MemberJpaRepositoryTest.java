package com.irostub.learnspringdatajpa.repository;

import com.irostub.learnspringdatajpa.entity.Member;
import com.irostub.learnspringdatajpa.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Test
    @DisplayName("맴버 등록 테스트 - Jpa")
    void save() {
        Member member = new Member("irostub", 19);
        Long memberId = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(memberId);

        assertEquals(member, findMember);
    }

    @Test
    @DisplayName("이름과 나이로 맴버 찾기 - Jpa")
    void findById() {
        Member member = new Member("irostub", 19);
        memberJpaRepository.save(member);

        List<Member> members = memberJpaRepository.findByUsernameAndAgeGreaterThan("irostub", 18);
        assertThat(members).contains(member);
    }

    @Test
    @DisplayName("맴버 단건 조회 - Jpa")
    void find() {
        Member member = new Member("irostub", 19);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(member.getId());
        assertEquals(member, findMember);
    }

    @Test
    @DisplayName("모든 맴버 조회 - Jpa")
    void findAll() {
        Member member = new Member("irostub", 19);
        memberJpaRepository.save(member);

        List<Member> members = memberJpaRepository.findAll();
        assertThat(members).contains(member);
    }

    @Test
    @DisplayName("맴버 카운트 - Jpa")
    void count() {
        Member member1 = new Member("irostub1", 19);
        Member member2 = new Member("irostub2", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        long count = memberJpaRepository.count();
        assertEquals(count, 2);
    }

    @Test
    @DisplayName("이름으로 맴버 찾기 - Jpa")
    void findByName() {
        Member member = new Member("irostub", 19);
        memberJpaRepository.save(member);

        List<Member> members = memberJpaRepository.findByName("irostub");
        assertThat(members).contains(member);
    }

    @Test
    @DisplayName("맴버 제거 - Jpa")
    void delete() {
        Member member = new Member("irostub", 19);
        memberJpaRepository.save(member);

        memberJpaRepository.delete(member);

        boolean isEmpty = memberJpaRepository.findById(member.getId()).isEmpty();
        assertTrue(isEmpty);
    }

    @Test
    @DisplayName("이름과 나이로 맴버 찾기 - Jpa")
    void findByUsernameAndAgeGreaterThan() {
        Member member = new Member("irostub", 19);
        memberJpaRepository.save(member);

        List<Member> members = memberJpaRepository.findByUsernameAndAgeGreaterThan("irostub", 10);
        assertThat(members).contains(member);
    }

    @Test
    @DisplayName("이름으로 맴버 찾기, 네임드 쿼리 사용 - Jpa")
    void findByUsername() {
        Member member = new Member("irostub", 19);
        memberJpaRepository.save(member);

        List<Member> members = memberJpaRepository.findByUsername("irostub");
        assertThat(members).contains(member);
    }

    @Test
    @DisplayName("페이징 처리 - Jpa")
    void findByPage() {
        Member member1 = new Member("irostub1", 11);
        Member member2 = new Member("irostub2", 12);
        Member member3 = new Member("irostub3", 13);
        Member member4 = new Member("irostub4", 14);
        Member member5 = new Member("irostub5", 15);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);

        List<Member> members = memberJpaRepository.findByPage(12, 0, 3);
        assertEquals(members.size(), 3);
        assertThat(members).contains(member2, member3, member4);
    }

    @Test
    @DisplayName("전체 카운트 - Jpa")
    void totalCount() {
        Member member1 = new Member("irostub1", 11);
        Member member2 = new Member("irostub2", 12);
        Member member3 = new Member("irostub3", 13);
        Member member4 = new Member("irostub4", 14);
        Member member5 = new Member("irostub5", 15);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);

        long count = memberJpaRepository.count();
        assertEquals(count, 5);
    }

    @Test
    @DisplayName("EntityGraph 탐색 테스트 - Jpa")
    void findAllWithTeam() {
        Team team1 = new Team("TeamA");
        Team team2 = new Team("TeamB");

        Member member1 = new Member("irostub1", 10);
        Member member2 = new Member("irostub2", 11);
        Member member3 = new Member("irostub3", 15);
        Member member4 = new Member("irostub4", 20);

        member1.changeTeam(team1);
        member2.changeTeam(team1);
        member3.changeTeam(team2);
        member4.changeTeam(team2);

        teamJpaRepository.save(team1);
        teamJpaRepository.save(team2);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);

        List<Member> members = memberJpaRepository.findAllWithTeam();
        assertThat(members).contains(member1, member2, member3, member4);
        for (Member member : members) {
            assertThat(member.getTeam().getClass()).isEqualTo(Team.class);
        }
    }
}