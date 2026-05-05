package com.example.ResumeAnalyzerPro_Final.controller;

import com.example.ResumeAnalyzerPro_Final.entity.User;
import com.example.ResumeAnalyzerPro_Final.entity.UserProfile;
import com.example.ResumeAnalyzerPro_Final.service.UserProfileService;
import com.example.ResumeAnalyzerPro_Final.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profileForm(HttpSession session, Model model) {
        User user = getSessionUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        UserProfile profile = userProfileService.getProfile(user);
        model.addAttribute("user", user);
        model.addAttribute("profile", profile);
        return "profile";
    }

    @PostMapping("/profile")
    public String saveProfile(@ModelAttribute("profile") UserProfile formProfile,
            @RequestParam(value = "resumeFile", required = false) MultipartFile resumeFile,
            HttpSession session,
            Model model) {
        User user = getSessionUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            UserProfile savedProfile = userProfileService.saveProfile(user, formProfile, resumeFile);
            User refreshedUser = userService.getUserById(user.getId());
            session.setAttribute("user", refreshedUser);
            model.addAttribute("user", refreshedUser);
            model.addAttribute("profile", savedProfile);
            model.addAttribute("success", "Profile saved successfully.");
            return "profile";
        } catch (RuntimeException ex) {
            formProfile.setEmail(user.getEmail());
            model.addAttribute("user", user);
            model.addAttribute("profile", formProfile);
            model.addAttribute("error", ex.getMessage());
            return "profile";
        }
    }

    @GetMapping("/profile/view")
    public String viewProfile(HttpSession session, Model model) {
        User user = getSessionUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        UserProfile profile = userProfileService.getProfile(user);
        model.addAttribute("user", user);
        model.addAttribute("profile", profile);
        return "profile-view";
    }

    private User getSessionUser(HttpSession session) {
        Object user = session.getAttribute("user");
        return user instanceof User ? (User) user : null;
    }
}
