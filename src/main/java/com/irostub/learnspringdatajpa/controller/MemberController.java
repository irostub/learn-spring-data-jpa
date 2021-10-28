package com.irostub.learnspringdatajpa.controller;

import com.irostub.learnspringdatajpa.entity.Member;
import com.irostub.learnspringdatajpa.repository.MemberRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberRepository memberRepository;

    //도메인 클래스 컨버터
    //도메인을 그대로 받는다. 트랜잭션 밖에서 조회 했으므로 dirty checking 이 되지 않는다.
    //단순 조회용으로만 사용할 것
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    //클라이언트에서 page, sort 정보를 전달하면 요청 파라미터 Pageable 로 받을 수 있다.
    //ex) /members?page=0&size=3&sort=id,desc&sort=username,desc
    //주의점은 페이지 인덱스가 0부터 시작한다는 점
    //페이징과 정렬
    @GetMapping("/members")
    public Page<MemberDto> list(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page.map(MemberDto::new);
    }

    //한 화면에 두개 이상의 페이징이 필요한 경우 다음과 같이 Qualifier 사용
    @GetMapping("/members-dual")
    public Object lists(@Qualifier("page1") Pageable pageable,
                        @Qualifier("page2") Pageable pageable2) {
        return null;
    }

    @Data
    private static class MemberDto{
        private Long id;
        private String username;

        public MemberDto(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
        }
    }
}
