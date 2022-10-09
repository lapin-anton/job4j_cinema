package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.exception.AlreadyBookedException;
import ru.job4j.cinema.exception.UserNotFoundException;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TicketRepository {

    private static final Logger LOG = LoggerFactory.getLogger(TicketRepository.class.getName());

    private final UserRepository userRepository;

    private final BasicDataSource pool;

    public TicketRepository(UserRepository userRepository, BasicDataSource pool) {
        this.userRepository = userRepository;
        this.pool = pool;
    }

    public Ticket addTicket(Session session, int row, int cell, User user) throws AlreadyBookedException {
        Ticket ticket = new Ticket(0, session, row, cell, user);
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO ticket(session_id, pos_row, cell, user_id) VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, session.getId());
            ps.setInt(2, row);
            ps.setInt(3, cell);
            ps.setInt(4, user.getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception during execution: ", e);
            throw new AlreadyBookedException(
                    String.format("Билет на сеанс: %s, ряд: %d место: %d уже забронирован.",
                            session.getName(), row, cell));
        }
        return ticket;
    }

    public List<Ticket> getTicketsBySessionId(Session session) {
        List<Ticket> rsl = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM ticket WHERE session_id = ?")
        ) {
            ps.setInt(1, session.getId());
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    rsl.add(generateTicket(session, it));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception during execution: ", e);
        }
        return rsl;
    }

    public List<Ticket> getTicketsBySessionIdAndPosRow(Session session, int posRow) {
        List<Ticket> rsl = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM ticket WHERE session_id = ? AND pos_row = ?")
        ) {
            ps.setInt(1, session.getId());
            ps.setInt(2, posRow);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    rsl.add(generateTicket(session, it));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception during execution: ", e);
        }
        return rsl;
    }

    private Ticket generateTicket(Session session, ResultSet it) throws SQLException, UserNotFoundException {
        return new Ticket(
                it.getInt("id"),
                session,
                it.getInt("pos_row"),
                it.getInt("cell"),
                getUser(it.getInt("user_id"))
        );
    }

    private User getUser(int userId) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findUserById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(String.format("Пользователь с id %d не найден.", userId));
        }
        return optionalUser.get();
    }

}
