package ru.job4j.dreamjob.persistence;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Post;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PostDbStoreTest {
    private static PostDbStore store;

    @BeforeClass
    public static void init() {
        store = new PostDbStore(new Main().loadPool());
    }

    @After
    public void clearTable() {
        store.clearTable();
    }

    @Test
    public void whenCreatePost() {
        Post post = new Post(0, "Java Job");
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenGetAllPostsFromDB() {
        List<Post> posts = List.of(
                new Post(1, "Junior"),
                new Post(2, "Middle"));
        for (Post post : posts) {
            store.add(post);
        }
        assertThat(store.findAll(), is(posts));
    }

    @Test
    public void whenUpdatePost() {
        Post post = new Post(2, "Middle");
        store.add(post);
        post.setName("Senior");
        store.update(post);
        assertThat(store.findById(2).getName(), is("Senior"));
    }
}