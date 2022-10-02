package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.exception.AlreadyBookedException;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@ThreadSafe
public class SessionController {

    private SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/formAddSession")
    public String addSession(Model model, HttpSession session) {
        User user = getUser(session);
        model.addAttribute("user", user);
        model.addAttribute("session", new Session(0, "Название киносеанса", null));
        return "addSession";
    }

    @PostMapping("/createSession")
    public String createSession(@ModelAttribute Session session,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        session.setPhoto(file.getBytes());
        sessionService.add(session);
        return "redirect:/";
    }

    @GetMapping("/formSelectRow/{sessionId}")
    public String formSelectRow(Model model, @PathVariable("sessionId") int id, HttpSession session) {
        User user = getUser(session);
        Session ssn = sessionService.findById(id);
        session.setAttribute("sid", id);
        model.addAttribute("user", user);
        model.addAttribute("s", ssn);
        model.addAttribute("rows", sessionService.getAvailableRows(ssn));
        return "choiceRow";
    }

    @GetMapping("/formSelectCell")
    public String formSelectCell(Model model,
                                 @RequestParam("r.id") int row,
                                 HttpSession session) {
        User user = getUser(session);
        session.setAttribute("row", row);
        model.addAttribute("user", user);
        model.addAttribute("s", sessionService.findById((Integer) session.getAttribute("sid")));
        model.addAttribute("row", session.getAttribute("row"));
        model.addAttribute("cells",
                sessionService.getAvailableCells(sessionService.findById((Integer) session.getAttribute("sid")),
                        (Integer) session.getAttribute("row")));
        return "choiceCell";
    }

    @PostMapping("/bookTicket")
    public String createTicket(Model model, @RequestParam("c.id") int cell, HttpSession httpSession) {
        try {
            Session session = sessionService.findById((Integer) httpSession.getAttribute("sid"));
            int row = (Integer) httpSession.getAttribute("row");
            User user = getUser(httpSession);
            model.addAttribute("user", user);
            model.addAttribute("ssn", session);
            model.addAttribute("row", row);
            model.addAttribute("cell", cell);
            Ticket ticket = sessionService.createTicket(session, row, cell, user);
            model.addAttribute("ticketId", ticket.getId());
        } catch (AlreadyBookedException e) {
            model.addAttribute("message", e.getMessage());
            return "bookFail";
        }
        return "bookSuccess";
    }

    private User getUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setEmail("Гость");
        }
        return user;
    }
}
