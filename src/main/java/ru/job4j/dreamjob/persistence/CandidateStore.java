package ru.job4j.dreamjob.persistence;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class CandidateStore {
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final AtomicInteger id = new AtomicInteger(3);

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Ivan Ivanov",
                "Middle Java dev",
                LocalDateTime.now().minusMinutes(1).format(formatter),
                new City(4, "Пермь")));
        candidates.put(2, new Candidate(2, "John Smith",
                "Junior Java dev",
                LocalDateTime.now().minusHours(3).format(formatter),
                new City(5, "Омск")));
        candidates.put(3, new Candidate(3, "Irina Stepanova",
                "Senior Java dev",
                LocalDateTime.now().minusDays(1).format(formatter),
                new City(1, "Москва")));
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public void add(Candidate candidate) {
        candidate.setId(id.incrementAndGet());
        candidate.setCreated(LocalDateTime.now().format(formatter));
        candidates.putIfAbsent(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public void update(Candidate candidate) {
        candidate.setCreated(LocalDateTime.now().format(formatter));
        candidates.replace(candidate.getId(), candidate);
    }
}
