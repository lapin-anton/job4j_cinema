package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.persistence.UserRepository;

import java.util.Optional;

@Service
@ThreadSafe
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByEmailOrPhone(String email, String phone) {
        return userRepository.findUserByEmailOrPhone(email, phone);
    }

    public Optional<User> add(User user) {
        return userRepository.add(user);
    }
}
