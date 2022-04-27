package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PostStore {
    private static final PostStore INST = new PostStore();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job",
                "Job for junior Java developer", LocalDateTime.now().minusMinutes(1).format(formatter)));
        posts.put(2, new Post(2, "Middle Java Job",
                "Job for middle Java developer", LocalDateTime.now().minusHours(3).format(formatter)));
        posts.put(3, new Post(3, "Senior Java Job",
                "Job for senior Java developer", LocalDateTime.now().minusDays(1).format(formatter)));
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void add(Post post) {
        posts.putIfAbsent(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public void update(Post post) {
        post.setCreated(LocalDateTime.now().format(formatter));
        posts.replace(post.getId(), post);
    }
}