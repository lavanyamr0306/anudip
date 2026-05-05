package com.example.ResumeAnalyzerPro_Final.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import com.example.ResumeAnalyzerPro_Final.entity.User;
import com.example.ResumeAnalyzerPro_Final.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService service;

    @GetMapping("/")
    public String home(HttpSession session) {
        return "landing";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {

        try {
            service.register(user);
            model.addAttribute("success", "Registration successful. Please log in.");
            return "login";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("user", user);
            return "register";
        }

    }

    @PostMapping("/login")
    public String login(String email, String password,
            HttpSession session,
            Model model) {

        User user = service.login(email, password);

        if (user == null) {

            model.addAttribute("error", "Invalid email or password");

            return "login";

        }

        if (user.isLocked()) {
            model.addAttribute("error", "Your account has been suspended by an administrator.");
            return "login";
        }

        session.setAttribute("user", user);

        if ("ADMIN".equals(user.getRole())) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/dashboard";

    }

    @GetMapping("/forgot-password")
    public String forgotPassword(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetForgottenPassword(@RequestParam("email") String email,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {
        try {
            service.resetPassword(email, newPassword, confirmPassword);
            model.addAttribute("success", "Password reset successful. Please log in with your new password.");
            return "login";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("email", email);
            return "forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPasswordWhileLoggedIn(@RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        User latestUser = service.findByEmail(sessionUser.getEmail());
        if (latestUser == null) {
            session.invalidate();
            return "redirect:/login";
        }

        if (!latestUser.getPassword().equals(currentPassword == null ? null : currentPassword.trim())) {
            model.addAttribute("error", "Current password is incorrect.");
            return "reset-password";
        }

        try {
            service.resetPassword(latestUser.getEmail(), newPassword, confirmPassword);
            latestUser = service.findByEmail(latestUser.getEmail());
            session.setAttribute("user", latestUser);
            model.addAttribute("success", "Password updated successfully.");
            return "reset-password";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "reset-password";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
