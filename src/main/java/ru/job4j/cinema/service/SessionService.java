package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.persistence.SessionPersistence;

import java.util.List;

@Service
@ThreadSafe
public class SessionService {

    private SessionPersistence sessionPersistence;

    public SessionService(SessionPersistence sessionPersistence) {
        this.sessionPersistence = sessionPersistence;
    }

    public List<Session> getSessions() {
        return sessionPersistence.findAll();
    }


    public Session findById(int id) {
        return sessionPersistence.findById(id);
    }

    public void buyTicket(Ticket ticket) {
        // todo
    }

    public List<Integer> getRows() {
        return List.of(1, 2, 3);
    }

    public List<Integer> getCells() {
        return List.of(1, 2, 3);
    }
}
