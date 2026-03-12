package com.mikeyy.nbafantasybackend.controller;

import com.mikeyy.nbafantasybackend.model.Player;
import com.mikeyy.nbafantasybackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(path = "api/players")  // plural is more RESTful
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * GET /api/v1/players
     * Supports filtering by query params (team, name, position)
     * Examples:
     *   GET /api/v1/players                  → all players
     *   GET /api/v1/players?team=Atlanta Hawks → players from team
     *   GET /api/v1/players?name=Trae        → name search
     *   GET /api/v1/players?position=PG      → position search
     *   GET /api/v1/players?team=Atlanta Hawks&position=PG → team + position
     */
    @GetMapping
    public ResponseEntity<List<Player>> getPlayers(
            @RequestParam(required = false) String team,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position) {

        return ResponseEntity.ok(resolvePlayers(team, name, position));
    }

    /**
     * GET /api/players/export
     * Returns the same player set as GET /api/players but as a downloadable CSV.
     */
    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<byte[]> exportPlayersCsv(
            @RequestParam(required = false) String team,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position) {

        List<Player> players = resolvePlayers(team, name, position);

        StringBuilder csv = new StringBuilder(1024);
        csv.append("name,team,position,age,pts,reb,ast,stl,blk,to,pf\n");
        for (Player p : players) {
            csv.append(csvCell(p.getName())).append(',')
                    .append(csvCell(p.getTeam())).append(',')
                    .append(csvCell(p.getPosition())).append(',')
                    .append(csvCell(p.getAge())).append(',')
                    .append(csvCell(p.getPts())).append(',')
                    .append(csvCell(p.getReb())).append(',')
                    .append(csvCell(p.getAst())).append(',')
                    .append(csvCell(p.getStl())).append(',')
                    .append(csvCell(p.getBlk())).append(',')
                    .append(csvCell(p.getTo())).append(',')
                    .append(csvCell(p.getPf()))
                    .append('\n');
        }

        String filename = "players.csv";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        return new ResponseEntity<>(csv.toString().getBytes(StandardCharsets.UTF_8), headers, HttpStatus.OK);
    }

    private List<Player> resolvePlayers(String team, String name, String position) {
        if (team != null && position != null) {
            return playerService.getPlayersByTeamAndPosition(team, position);
        } else if (team != null) {
            return playerService.getPlayersByTeam(team);
        } else if (name != null) {
            return playerService.getPlayersByName(name);
        } else if (position != null) {
            return playerService.getPlayersByPosition(position);
        } else {
            return playerService.getPlayers();
        }
    }

    private static String csvCell(Object value) {
        if (value == null) {
            return "";
        }
        String s = String.valueOf(value);
        boolean needsQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        if (!needsQuotes) {
            return s;
        }
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }

    /**
     * POST /api/v1/players
     * Creates a new player
     */
    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        Player created = playerService.addPlayer(player);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /api/v1/players
     * Updates an existing player (identified by name in the body)
     */
    @PutMapping
    public ResponseEntity<Player> updatePlayer(@RequestBody Player updatedPlayer) {
        Player result = playerService.updatePlayer(updatedPlayer);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    /**
     * DELETE /api/v1/players/{name}
     * Deletes a player by name
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<String> deletePlayer(@PathVariable String name) {
        playerService.deletePlayer(name);
        return ResponseEntity.ok("Player '" + name + "' deleted successfully");
    }
}
