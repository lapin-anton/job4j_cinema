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
public class SessionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SessionRepository.class.getName());

    private final BasicDataSource pool;

    public SessionRepository(BasicDataSource pool) {
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
                            it.getString("name"),
                            it.getBytes("photo")
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
                            it.getString("name"),
                            it.getBytes("photo"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception during execution: ", e);
        }
        return null;
    }

    public Session add(Session session) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO sessions(name, photo) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, session.getName());
            ps.setBytes(2, session.getPhoto());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    session.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception during execution: ", e);
        }
        return session;
    }
}
