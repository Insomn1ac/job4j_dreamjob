package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.persistence.UserDbStore;

import java.util.List;

@Service
@ThreadSafe
public class UserService {
    private final UserDbStore store;

    public UserService(UserDbStore store) {
        this.store = store;
    }

    public User add(User user) {
        return store.add(user);
    }

    public User findUserByName(String name) {
        return store.findUserByName(name);
    }

    public List<User> findAll() {
        return store.findAll();
    }
}
