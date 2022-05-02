package ru.job4j.dreamjob.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDbStore {
    private final BasicDataSource pool;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PostDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM post ORDER BY created DESC")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(new Post(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("created"),
                            rs.getBoolean("visible"),
                            new City(rs.getInt("city_id"))));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public Post add(Post post) {
        try (Connection connection = pool.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO post(name, description, created, visible, city_id) VALUES (?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, post.getName());
            stmt.setString(2, post.getDescription());
            stmt.setString(3, LocalDateTime.now().format(formatter));
            stmt.setBoolean(4, post.isVisible());
            stmt.setInt(5, post.getCity().getId());
            stmt.execute();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    post.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    public Post findById(int id) {
        try (Connection connection = pool.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM post WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Post(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Post post) {
        try (Connection connection = pool.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "UPDATE post SET name = ?, description = ?, city_id = ?, created = ?, visible = ? WHERE id = ?")) {
            stmt.setString(1, post.getName());
            stmt.setString(2, post.getDescription());
            stmt.setInt(3, post.getCity().getId());
            stmt.setString(4, LocalDateTime.now().format(formatter));
            stmt.setBoolean(5, post.isVisible());
            stmt.setInt(6, post.getId());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearTable() {
        try (Connection connection = pool.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM post")) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
