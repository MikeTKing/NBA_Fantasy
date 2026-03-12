package com.mikeyy.nbafantasybackend.repository;

import com.mikeyy.nbafantasybackend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

    Optional<Player> findByName(String name);

    List<Player> findByTeamIgnoreCase(String team);

    List<Player> findByNameContainingIgnoreCase(String name);

    List<Player> findByPositionContainingIgnoreCase(String position);

    List<Player> findByTeamIgnoreCaseAndPositionIgnoreCase(String team, String position);

    void deleteByName(String name);

    boolean existsByName(String name);
}
