package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.exception.AlreadyBookedException;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.SessionRepository;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@ThreadSafe
public class SessionService {

    private SessionRepository sessionRepository;

    private TicketRepository ticketRepository;

    public SessionService(SessionRepository sessionRepository, TicketRepository ticketRepository) {
        this.sessionRepository = sessionRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Session> getSessions() {
        return sessionRepository.findAll();
    }

    public Session findById(int id) {
        return sessionRepository.findById(id);
    }

    public void add(Session session) {
        sessionRepository.add(session);
    }

    public List<Integer> getAvailableRows(Session session) {
        List<Ticket> booked = ticketRepository.getTicketsBySessionId(session);
        Map<Integer, List<Integer>> unbookedCellMap = prepareCellMap();
        booked.forEach(t -> unbookedCellMap.get(t.getPosRow()).remove((Integer) t.getCell()));
        List<Integer> availableRows = unbookedCellMap.entrySet().stream()
                .filter(row -> row.getValue().size() > 0).map(Map.Entry::getKey).collect(Collectors.toList());
        return availableRows;
    }

    public List<Integer> getAvailableCells(Session session, int row) {
        List<Ticket> booked = ticketRepository.getTicketsBySessionIdAndPosRow(session, row);
        List<Integer> availableCells = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        booked.forEach(t -> availableCells.remove((Integer) t.getCell()));
        return availableCells;
    }

    public Ticket createTicket(Session session, int row, int cell, User user) throws AlreadyBookedException {
        return ticketRepository.addTicket(session, row, cell, user);
    }

    private Map<Integer, List<Integer>> prepareCellMap() {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int row = 1; row <= 10; row++) {
            map.put(row, new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
        }
        return map;
    }
}
