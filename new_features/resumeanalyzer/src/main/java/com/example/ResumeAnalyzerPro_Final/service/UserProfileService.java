package com.example.ResumeAnalyzerPro_Final.service;

import com.example.ResumeAnalyzerPro_Final.entity.User;
import com.example.ResumeAnalyzerPro_Final.entity.UserProfile;
import com.example.ResumeAnalyzerPro_Final.repository.UserProfileRepository;
import com.example.ResumeAnalyzerPro_Final.repository.UserRepository;
import com.example.ResumeAnalyzerPro_Final.util.TikaParser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TikaParser tikaParser;

    public UserProfile getProfile(User user) {
        return userProfileRepository.findByUser(user).orElseGet(() -> buildDefaultProfile(user));
    }

    public UserProfile getExistingProfile(User user) {
        return userProfileRepository.findByUser(user).orElse(null);
    }

    @Transactional
    public UserProfile saveProfile(User user,
            UserProfile formProfile,
            MultipartFile resumeFile) {

        UserProfile profile = userProfileRepository.findByUser(user).orElseGet(() -> {
            UserProfile freshProfile = new UserProfile();
            freshProfile.setUser(user);
            return freshProfile;
        });

        String normalizedName = normalizeRequired(formProfile.getName(), "Name is required.");
        profile.setName(normalizedName);
        profile.setEmail(user.getEmail());
        profile.setProfessionalTitle(normalizeOptional(formProfile.getProfessionalTitle()));
        profile.setMobileNumber(normalizeOptional(formProfile.getMobileNumber()));
        profile.setCollegeName(normalizeOptional(formProfile.getCollegeName()));
        profile.setDegree(normalizeOptional(formProfile.getDegree()));
        profile.setDepartment(normalizeOptional(formProfile.getDepartment()));
        profile.setCgpa(normalizeOptional(formProfile.getCgpa()));
        profile.setLinkedinProfile(normalizeOptional(formProfile.getLinkedinProfile()));
        profile.setGithubProfile(normalizeOptional(formProfile.getGithubProfile()));
        profile.setPortfolioWebsite(normalizeOptional(formProfile.getPortfolioWebsite()));

        if (resumeFile != null && !resumeFile.isEmpty()) {
            validateResumeFile(resumeFile);
            try {
                String parsedContent = tikaParser.parse(resumeFile.getInputStream());
                if (parsedContent == null || parsedContent.trim().isEmpty()) {
                    throw new RuntimeException("Uploaded resume could not be read. Please upload a valid PDF or Word file.");
                }
                profile.setResumeFileName(resumeFile.getOriginalFilename());
                profile.setResumeContentType(resumeFile.getContentType());
                profile.setResumeData(resumeFile.getBytes());
                profile.setResumeText(parsedContent.trim());
            } catch (IOException ex) {
                throw new RuntimeException("Unable to save resume file. Please try again.");
            }
        }

        user.setName(normalizedName);
        userRepository.save(user);

        return userProfileRepository.save(profile);
    }

    private UserProfile buildDefaultProfile(User user) {
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setName(user.getName());
        profile.setEmail(user.getEmail());
        return profile;
    }

    private void validateResumeFile(MultipartFile resumeFile) {
        String fileName = resumeFile.getOriginalFilename() == null ? "" : resumeFile.getOriginalFilename().toLowerCase();
        boolean validExtension = fileName.endsWith(".pdf")
                || fileName.endsWith(".doc")
                || fileName.endsWith(".docx");
        if (!validExtension) {
            throw new RuntimeException("Resume must be uploaded as a PDF or Word document.");
        }
    }

    private String normalizeRequired(String value, String message) {
        if (value == null || value.trim().isBlank()) {
            throw new RuntimeException(message);
        }
        return value.trim();
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
