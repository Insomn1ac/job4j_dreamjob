package ru.job4j.dreamjob.persistence;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CandidateDbStoreTest {
    private static CandidateDbStore store;

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
        Candidate candidate = new Candidate(0, "Ivan Ivanov");
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
    }

    @Test
    public void whenGetAllPostsFromDB() {
        List<Candidate> posts = List.of(
                new Candidate(1, "Ivan Ivanov"),
                new Candidate(2, "Sergey Petrov"));
        for (Candidate candidate : posts) {
            store.add(candidate);
        }
        assertThat(store.findAll(), is(posts));
    }

    @Test
    public void whenUpdateCandidate() {
        Candidate candidate = new Candidate(2, "Ivan Ivanov");
        store.add(candidate);
        candidate.setName("Vyacheslav Stepanov");
        store.update(candidate);
        assertThat(store.findById(2).getName(), is("Vyacheslav Stepanov"));
    }
}