package ru.job4j.cinema.exception;

public class AlreadyBookedException extends Exception {
    public AlreadyBookedException(String message) {
        super(message);
    }
}
