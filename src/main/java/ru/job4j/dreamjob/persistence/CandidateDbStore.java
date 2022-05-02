package ru.job4j.dreamjob.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateDbStore {
    private final BasicDataSource pool;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CandidateDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection connection = pool.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM candidate ORDER BY created DESC")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    candidates.add(new Candidate(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("created"),
                            rs.getBoolean("visible"),
                            new City(rs.getInt("city_id")),
                            rs.getBytes("photo")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidates;
    }

    public Candidate add(Candidate candidate) {
        try (Connection connection = pool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO candidate(name, description, created, visible, city_id, photo) VALUES (?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, candidate.getName());
            stmt.setString(2, candidate.getDesc());
            stmt.setString(3, LocalDateTime.now().format(formatter));
            stmt.setBoolean(4, candidate.isVisible());
            stmt.setInt(5, candidate.getCity().getId());
            stmt.setBytes(6, candidate.getPhoto());
            stmt.execute();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    candidate.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidate;
    }

    public Candidate findById(int id) {
        try (Connection connection = pool.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM candidate WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Candidate(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("created"),
                            rs.getBoolean("visible"),
                            new City(rs.getInt("city_id")),
                            rs.getBytes("photo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Candidate candidate) {
        try (Connection connection = pool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE candidate SET name = ?, description = ?, city_id = ?, created = ?, visible = ? WHERE id = ?")) {
            stmt.setString(1, candidate.getName());
            stmt.setString(2, candidate.getDesc());
            stmt.setInt(3, candidate.getCity().getId());
            stmt.setString(4, LocalDateTime.now().format(formatter));
            stmt.setBoolean(5, candidate.isVisible());
            stmt.setInt(6, candidate.getId());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearTable() {
        try (Connection connection = pool.getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM candidate")) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
