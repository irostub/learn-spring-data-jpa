package com.irostub.learnspringdatajpa.repository.member;

import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String username;
    private int age;

    protected MemberDto() {
    }

    public MemberDto(Long id, String username, int age) {
        this.id = id;
        this.username = username;
        this.age = age;
    }
}
