package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketPersistence {

    private static final Logger LOG = LoggerFactory.getLogger(TicketPersistence.class.getName());

    private final BasicDataSource pool;

    public TicketPersistence(BasicDataSource pool) {
        this.pool = pool;
    }

    public void addTicket(Session session, int row, int cell, User user) {
        // todo
    }

}
