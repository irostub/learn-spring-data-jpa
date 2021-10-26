package com.irostub.learnspringdatajpa.repository;

import com.irostub.learnspringdatajpa.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TeamJpaRepository {
    private final EntityManager em;

    public Long save(Team team) {
        em.persist(team);
        return team.getId();
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public Team find(Long id) {
        return em.find(Team.class, id);
    }

    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class).getResultList();
    }

    public Optional<Team> findById(Long id) {
        Team team = em.find(Team.class, id);
        return Optional.ofNullable(team);
    }
}
