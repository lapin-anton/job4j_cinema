package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SessionPersistence {

    private static final Logger LOG = LoggerFactory.getLogger(SessionPersistence.class.getName());

    private final BasicDataSource pool;

    public SessionPersistence(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Session> findAll() {
        List<Session> sessions = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM sessions")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    sessions.add(new Session(
                            it.getInt("id"),
                            it.getString("name")
                        )
                    );
                }
            }
        } catch (Exception e) {
            LOG.error("Exception during execution: ", e);
        }
        return sessions;
    }

    public Session findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM sessions WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Session(
                            it.getInt("id"),
                            it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception during execution: ", e);
        }
        return null;
    }
}
