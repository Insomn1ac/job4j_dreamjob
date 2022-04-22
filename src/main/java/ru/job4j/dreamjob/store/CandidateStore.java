package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {
    private static final CandidateStore INST = new CandidateStore();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private CandidateStore() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        candidates.put(1, new Candidate(1, "Ivan Ivanov",
                "Middle Java dev", LocalDateTime.now().minusMinutes(1).format(formatter)));
        candidates.put(2, new Candidate(2, "John Smith",
                "Junior Java dev", LocalDateTime.now().minusHours(3).format(formatter)));
        candidates.put(3, new Candidate(3, "Irina Stepanova",
                "Senior Java dev", LocalDateTime.now().minusDays(1).format(formatter)));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
