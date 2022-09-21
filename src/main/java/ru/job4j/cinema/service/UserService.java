package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.persistence.UserPersistence;

import java.util.Optional;

@Service
@ThreadSafe
public class UserService {

    private final UserPersistence userPersistence;

    public UserService(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    public Optional<User> findUserByEmailOrPhone(String email, String phone) {
        return userPersistence.findUserByEmailOrPhone(email, phone);
    }

    public Optional<User> add(User user) {
        return userPersistence.add(user);
    }
}
