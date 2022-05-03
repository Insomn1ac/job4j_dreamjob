package ru.job4j.dreamjob.persistence;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UserDbStoreTest {
    private static UserDbStore store;

    @BeforeClass
    public static void init() {
        store = new UserDbStore(new Main().loadPool());
    }

    @After
    public void clearTable() {
        store.clearTable();
    }

    @Test
    public void whenCreateUser() {
        User user = new User(0, "Ivan Ivanov", "Java", new char[] {'a', 's', 'd', 'f'});
        store.add(user);
        User userInDb = store.findUserByName(user.getName());
        assertThat(userInDb.getName(), is(user.getName()));
    }

    @Test
    public void whenGetAllUsersFromDB() {
        List<User> users = List.of(
                new User(0, "Ivan Ivanov", "java@ya.ru", new char[] {'a', 's', 'd', 'f'}),
                new User(1, "Petr Petrov", "mail@gmail.com", new char[] {'q', 'w', 'e'}));
        for (User user : users) {
            store.add(user);
        }
        assertThat(store.findAll(), is(users));
    }
}