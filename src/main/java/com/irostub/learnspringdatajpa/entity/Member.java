package com.irostub.learnspringdatajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NamedEntityGraphs({
    @NamedEntityGraph(
            name = "Member.all",
            attributeNodes = @NamedAttributeNode("team")
    )
})

@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username like concat('%',:username,'%')")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
