package ru.job4j.dreamjob.persistence;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class PostStore {
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final AtomicInteger id = new AtomicInteger(3);

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job",
                "Job for junior Java developer",
                LocalDateTime.now().minusMinutes(1).format(formatter),
                false,
                new City(1, "Москва")));
        posts.put(2, new Post(2, "Middle Java Job",
                "Job for middle Java developer",
                LocalDateTime.now().minusHours(3).format(formatter),
                false,
                new City(2, "Санкт-Петербург")));
        posts.put(3, new Post(3, "Senior Java Job",
                "Job for senior Java developer",
                LocalDateTime.now().minusDays(1).format(formatter),
                false,
                new City(5, "Омск")));
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void add(Post post) {
        post.setId(id.incrementAndGet());
        post.setCreated(LocalDateTime.now().format(formatter));
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