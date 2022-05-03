package ru.job4j.dreamjob.persistence;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CandidateDbStoreTest {
    private static CandidateDbStore store;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeClass
    public static void init() {
        store = new CandidateDbStore(new Main().loadPool());
    }

    @After
    public void clearTable() {
        store.clearTable();
    }

    @Test
    public void whenCreateCandidate() {
        Candidate candidate = new Candidate(0,
                "Ivan Ivanov",
                "Senior C++ dev",
                "yesterday",
                true,
                new City()
        );
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
    }

    @Test
    public void whenGetAllPostsFromDB() {
        List<Candidate> candidates = List.of(new Candidate(1,
                        "Ivan Ivanov",
                        "Senior C++ dev",
                        "yesterday",
                        true,
                        new City()
                ),
                new Candidate(2,
                        "Sergey Petrov",
                        "Junior Java dev",
                        "today",
                        false,
                        new City())
        );
        for (Candidate candidate : candidates) {
            store.add(candidate);
        }
        assertThat(store.findAll(), is(candidates));
    }

    @Test
    public void whenUpdateCandidate() {
        Candidate candidate = new Candidate(2,
                "Ivan Ivanov",
                "Senior C++ dev",
                LocalDateTime.now().format(formatter),
                true,
                new City()
        );
        store.add(candidate);
        Candidate updatedCandidate = new Candidate(candidate.getId(),
                "Vyacheslav Stepanov",
                "Senior C++ dev",
                LocalDateTime.now().format(formatter),
                true,
                candidate.getCity()
        );
        store.update(updatedCandidate);
        assertThat(store.findById(candidate.getId()).getName(), is("Vyacheslav Stepanov"));
    }
}