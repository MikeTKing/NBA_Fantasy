package com.mikeyy.nbafantasybackend.service;

import com.mikeyy.nbafantasybackend.model.Player;
import com.mikeyy.nbafantasybackend.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvPlayerImportService {

    private final PlayerRepository playerRepository;

    public CsvPlayerImportService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public int importPlayers(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CSV file is required");
        }

        try {
            return importPlayers(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file", e);
        }
    }

    public int importPlayers(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV input stream is required");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.isBlank()) {
                throw new IllegalArgumentException("CSV header row is required");
            }

            String[] headers = splitCsvLine(headerLine);
            Map<String, Integer> index = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                index.putIfAbsent(normalizeHeader(headers[i]), i);
            }

            List<Player> players = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] cols = splitCsvLine(line);
                String name = firstNonBlank(
                        getString(cols, index, "name"),
                        getString(cols, index, "player"),
                        getString(cols, index, "playername")
                );
                if (isBlank(name) || "Squad Total".equalsIgnoreCase(name)) {
                    continue;
                }

                Player player = new Player();
                player.setName(name);
                player.setTeam(firstNonBlank(
                        getString(cols, index, "team"),
                        getString(cols, index, "teamname")
                ));
                player.setPosition(firstNonBlank(
                        getString(cols, index, "position"),
                        getString(cols, index, "pos")
                ));
                player.setAge(getInteger(cols, index, "age"));
                player.setPts(firstNonNull(
                        getDouble(cols, index, "pts"),
                        getDouble(cols, index, "ppg")
                ));
                player.setReb(firstNonNull(
                        getDouble(cols, index, "reb"),
                        getDouble(cols, index, "rpg")
                ));
                player.setAst(firstNonNull(
                        getDouble(cols, index, "ast"),
                        getDouble(cols, index, "apg")
                ));
                player.setStl(firstNonNull(
                        getDouble(cols, index, "stl"),
                        getDouble(cols, index, "spg")
                ));
                player.setBlk(firstNonNull(
                        getDouble(cols, index, "blk"),
                        getDouble(cols, index, "bpg")
                ));
                player.setTo(firstNonNull(
                        getDouble(cols, index, "to"),
                        getDouble(cols, index, "tov"),
                        getDouble(cols, index, "turnovers")
                ));
                player.setPf(firstNonNull(
                        getDouble(cols, index, "pf"),
                        getDouble(cols, index, "fouls")
                ));
                players.add(player);
            }

            playerRepository.saveAll(players);
            return players.size();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file", e);
        }
    }

    private static String[] splitCsvLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
                continue;
            }

            if (c == ',' && !inQuotes) {
                out.add(current.toString());
                current.setLength(0);
                continue;
            }

            current.append(c);
        }

        out.add(current.toString());
        return out.toArray(String[]::new);
    }

    private static String getString(String[] cols, Map<String, Integer> index, String key) {
        Integer i = index.get(normalizeHeader(key));
        if (i == null || i >= cols.length) {
            return null;
        }

        String raw = cols[i].trim();
        return raw.isEmpty() ? null : raw;
    }

    private static Double getDouble(String[] cols, Map<String, Integer> index, String key) {
        String raw = getString(cols, index, key);
        return raw == null ? null : parseDouble(raw);
    }

    private static Integer getInteger(String[] cols, Map<String, Integer> index, String key) {
        String raw = getString(cols, index, key);
        return raw == null ? null : parseInteger(raw);
    }

    private static String normalizeHeader(String header) {
        if (header == null) {
            return "";
        }

        return header.trim()
                .toLowerCase()
                .replace("_", "")
                .replace(" ", "");
    }

    private static Double parseDouble(String raw) {
        String cleaned = extractFirstNumber(raw);
        return cleaned == null ? null : Double.valueOf(cleaned);
    }

    private static Integer parseInteger(String raw) {
        String cleaned = extractFirstNumber(raw);
        if (cleaned == null) {
            return null;
        }

        return (int) Math.floor(Double.parseDouble(cleaned));
    }

    private static String extractFirstNumber(String raw) {
        if (raw == null) {
            return null;
        }

        String value = raw.trim();
        if (value.isEmpty()) {
            return null;
        }

        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("[-+]?(?:\\d+\\.?\\d*|\\.\\d+)")
                .matcher(value);
        if (!matcher.find()) {
            return null;
        }

        return matcher.group();
    }

    @SafeVarargs
    private static <T> T firstNonNull(T... values) {
        if (values == null) {
            return null;
        }

        for (T value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }

        for (String value : values) {
            if (!isBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
