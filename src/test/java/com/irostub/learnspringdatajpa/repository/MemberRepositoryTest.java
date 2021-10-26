package com.irostub.learnspringdatajpa.repository;

import com.irostub.learnspringdatajpa.entity.Member;
import com.irostub.learnspringdatajpa.entity.Team;
import com.irostub.learnspringdatajpa.repository.member.MemberDto;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("이름과 나이로 맴버 찾기 - Spring Data Jpa")
    void findByUsernameAndAgeGreaterThan() {
        Member member = new Member("irostub", 19);
        memberRepository.save(member);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("irostub", 18);
        assertThat(members).contains(member);
    }

    @Test
    @DisplayName("이름으로 맴버 찾기 - Spring Data Jpa")
    void findByUsernameLike() {
        Member member = new Member("irostub", 19);
        memberRepository.save(member);

        List<Member> members = memberRepository.findByUsername("irostub");
        assertThat(members).contains(member);
    }

    @Test
    @DisplayName("이름으로 맴버 찾기, 네임드 쿼리 사용 - Spring Data Jpa")
    void findByUsername() {
        Member member = new Member("irostub", 19);
        memberRepository.save(member);

        List<Member> members = memberRepository.findByUsername("irostub");
        assertThat(members).contains(member);
    }

    @Test
    @DisplayName("이름과 나이로 맴버 찾기, 무명 네임드 쿼리 사용 - Spring Data Jpa")
    void findUser() {
        Member member = new Member("irostub", 19);
        memberRepository.save(member);

        List<Member> members = memberRepository.findUser("irostub", 18);
        assertThat(members).contains(member);
    }

    @Test
    @DisplayName("모든 유저 이름 찾기 - Spring Data Jpa")
    void findAllUsername() {
        Member member = new Member("irostub", 19);
        memberRepository.save(member);

        List<String> memberNames = memberRepository.findAllUsername();
        assertThat(memberNames).contains(member.getUsername());
    }

    @Test
    @DisplayName("DTO 로 즉시 조회 - Spring Data Jpa")
    void findMemberDto() {
        Member member = new Member("irostub", 19);
        memberRepository.save(member);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();
        MemberDto memberDto = memberDtos.get(0);
        assertEquals(memberDto.getUsername(), "irostub");
        assertEquals(memberDto.getAge(), 19);
    }

    @Test
    @DisplayName("나이에 부합하는 맴버 수 조회 - Spring Data Jpa")
    void countByAgeGreaterThanEqual() {
        addManyMember();

        long count = memberRepository.countByAgeGreaterThanEqual(15);
        assertEquals(count, 2);
    }

    @Test
    @DisplayName("페이징 쿼리 테스트 - Spring Data Jpa")
    void findByAgeGreaterThanEqual() {
        addManyMember();

        PageRequest pageRequest = PageRequest.of(0, 2, Sort.Direction.ASC, "username");
        Page<Member> pageMembers = memberRepository.findByAgeGreaterThanEqual(11, pageRequest);

        assertEquals(pageMembers.getContent().size(), 2);
        assertEquals(pageMembers.getTotalElements(),3);
        assertEquals(pageMembers.getNumber(), 0);
        assertEquals(pageMembers.getTotalPages(),2);
        assertTrue(pageMembers.isFirst());
        assertTrue(pageMembers.hasNext());
    }

    @Test
    @DisplayName("벌크 업데이트 쿼리 테스트 - Spring Data Jpa")
    void bulkUpdateAgePlusOne() {
        addManyMember();

        int resultCount = memberRepository.bulkUpdateAgePlusOne(15);
        assertEquals(resultCount, 2);
    }

    @Test
    @DisplayName("EntityGraph 탐색 테스트 - Spring Data Jpa")
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

        teamRepository.save(team1);
        teamRepository.save(team2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<Member> members = memberRepository.findAllWithTeam();
        assertThat(members).contains(member1, member2, member3, member4);
        for (Member member : members) {
            assertThat(member.getTeam().getClass()).isEqualTo(Team.class);
        }
    }

    @Test
    @DisplayName("hint 를 사용한 readOnly 객체 조회")
    void findReadOnlyByUsername() {
        Member member = new Member("irostub", 10);
        memberRepository.save(member);

        Team team = new Team("TeamA");
        teamRepository.save(team);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("irostub");
        findMember.changeTeam(team);

        em.flush();
        em.clear();

        Member lastMember = memberRepository.findReadOnlyByUsername("irostub");
        Hibernate.initialize(lastMember.getTeam());
        assertNull(lastMember.getTeam());
    }

    private void addManyMember() {
        Member member1 = new Member("irostub1", 10);
        Member member2 = new Member("irostub2", 11);
        Member member3 = new Member("irostub3", 15);
        Member member4 = new Member("irostub4", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
    }
}
