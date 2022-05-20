package ru.job4j.dreamjob.persistence;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@ThreadSafe
public class UserDbStore {
    private final BasicDataSource pool;

    public UserDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        try (Connection connection = pool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO users(name, email, password) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, String.valueOf(user.getPassword()));
            stmt.execute();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            user = null;
        }
        return Optional.ofNullable(user);
    }

    public User findUserByName(String name) {
        try (Connection connection = pool.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT id, name, email FROM users WHERE name = ?")) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        Optional<User> rsl = Optional.empty();
        try (Connection connection = pool.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT name FROM users WHERE email = ? AND password = ?")) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    rsl = Optional.of(
                            new User(rs.getString("name"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT id, name, email FROM users")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void clearTable() {
        try (Connection connection = pool.getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM users")) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
