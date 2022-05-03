package ru.job4j.dreamjob.persistence;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
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
        Post post = new Post(0, "Java Job", "Java", "today", true, new City());
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenGetAllPostsFromDB() {
        List<Post> posts = List.of(
                new Post(1, "Junior", "Junior Java job", "two days ago", true, new City()),
                new Post(2, "Middle", "Middle Java job", "yesterday", false, new City()));
        for (Post post : posts) {
            store.add(post);
        }
        assertThat(store.findAll(), is(posts));
    }

    @Test
    public void whenUpdatePost() {
        Post post = new Post(2,
                "Middle",
                "Middle Java job",
                "in last month",
                true,
                new City()
        );
        store.add(post);
        Post updated = new Post(post.getId(),
                "Senior",
                "Senior Java job",
                "in last month",
                true,
                post.getCity()
        );
        store.update(updated);
        assertThat(store.findById(post.getId()).getName(), is("Senior"));
    }
}