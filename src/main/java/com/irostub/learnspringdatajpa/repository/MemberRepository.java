package com.irostub.learnspringdatajpa.repository;

import com.irostub.learnspringdatajpa.entity.Member;
import com.irostub.learnspringdatajpa.repository.member.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //@Query 선언 없이 메서드 명으로 네임드 쿼리 호출
    List<Member> findByUsernameLike(@Param("username") String username);

    //namedQuery 사용
    //@Query(name = "findByUsername")
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    //쿼리 메서드의 존재로 네임드 쿼리를 딱히 사용할 필요가 없으므로 무명 네임드 쿼리 정도를 사용
    @Query("select m from Member m where m.username=:username and m.age>:age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findAllUsername();

    @Query("select new com.irostub.learnspringdatajpa.repository.member.MemberDto(m.id, m.username, m.age) from Member m")
    List<MemberDto> findMemberDto();

    long countByAgeGreaterThanEqual(int age);

    @QueryHints(value=@QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Page<Member> findByAgeGreaterThanEqual(int age, Pageable pageable);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Member m set m.age=m.age+1 where m.age>= :age")
    int bulkUpdateAgePlusOne(@Param("age") int age);

    //EntityGraph 축약해서 사용 시 EntityGraph 어노테이션에 attributePaths 반드시 추가(연관관계 필드)
    //EntityGraph 에 Entity 에서 정의한 NamedEntityGraph 의 name 을 넣어줄 수도 있다
    /*@EntityGraph("Member.all")*/
    @EntityGraph(attributePaths = {"team"})
    @Query(value="select m from Member m")
    List<Member> findAllWithTeam();

    //쿼리 힌트
    @QueryHints(value=@QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    //LOCKING
    //낙관적 락과 비관적 락의 종류와 차이에 대해서 더 알아봐야함
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select m from Member m")
    List<Member> findWithLockBy();



    //count 쿼리와 select 쿼리를 분할하여 적용 가능
    //최적화 시 count 가 수많은 조인을 탈 필요는 없을 때 필요, 최적화
    /*@Query(value="select m from Member m where m.age>=:age",
            countQuery ="select count(m) from Member m where m.age>=:age")
    Page<Member> findByAgeGreaterThanEqual(int age, Pageable pageable);*/
}
