package com.mikeyy.nbafantasybackend.service;

import com.mikeyy.nbafantasybackend.model.Player;
import com.mikeyy.nbafantasybackend.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // Get all players
    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    // Get players by team name
    public List<Player> getPlayersByTeam(String teamName) {
        return playerRepository.findByTeamIgnoreCase(teamName);
    }

    // Search players by name (partial match, case-insensitive)
    public List<Player> getPlayersByName(String searchText) {
        return playerRepository.findByNameContainingIgnoreCase(searchText);
    }

    // Search players by position (partial match, case-insensitive)
    public List<Player> getPlayersByPosition(String searchText) {
        return playerRepository.findByPositionContainingIgnoreCase(searchText);
    }

    // Get players from a specific team AND position
    public List<Player> getPlayersByTeamAndPosition(String team, String position) {
        return playerRepository.findByTeamIgnoreCaseAndPositionIgnoreCase(team, position);
    }

    // Add a new player
    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    // Update existing player (using name as lookup key)
    public Player updatePlayer(Player updatedPlayer) {
        Optional<Player> existing = playerRepository.findByName(updatedPlayer.getName());
        if (existing.isPresent()) {
            Player playerToUpdate = existing.get();

            // Update only the fields you allow to be changed
            playerToUpdate.setTeam(updatedPlayer.getTeam());
            playerToUpdate.setPosition(updatedPlayer.getPosition());
            playerToUpdate.setAge(updatedPlayer.getAge());
            playerToUpdate.setPts(updatedPlayer.getPts());
            playerToUpdate.setReb(updatedPlayer.getReb());
            playerToUpdate.setAst(updatedPlayer.getAst());
            playerToUpdate.setStl(updatedPlayer.getStl());
            playerToUpdate.setBlk(updatedPlayer.getBlk());
            playerToUpdate.setTo(updatedPlayer.getTo());
            playerToUpdate.setPf(updatedPlayer.getPf());

            // name is used as identifier, so we usually don't change it
            // if you want to allow name change, add: playerToUpdate.setName(updatedPlayer.getName());

            return playerRepository.save(playerToUpdate);
        }
        return null; // or throw exception if not found
    }

    @Transactional
    public void deletePlayer(String playerName) {
        playerRepository.deleteByName(playerName);
    }

    // Optional: check if player exists by name
    public boolean existsByName(String name) {
        return playerRepository.existsByName(name);
    }
}