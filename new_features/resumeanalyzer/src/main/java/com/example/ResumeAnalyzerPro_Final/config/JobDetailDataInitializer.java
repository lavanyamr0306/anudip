package com.example.ResumeAnalyzerPro_Final.config;

import com.example.ResumeAnalyzerPro_Final.entity.JobDetail;
import com.example.ResumeAnalyzerPro_Final.repository.JobDetailRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobDetailDataInitializer implements CommandLineRunner {

    private final JobDetailRepository jobDetailRepository;

    public JobDetailDataInitializer(JobDetailRepository jobDetailRepository) {
        this.jobDetailRepository = jobDetailRepository;
    }

    @Override
    public void run(String... args) {
        List<JobDetail> defaults = List.of(
                buildJobDetail(
                        "Backend Java Developer",
                        "TechNova Solutions",
                        "Bengaluru, India",
                        "INR 9-14 LPA",
                        "Java, Spring Boot, REST APIs, MySQL, Hibernate",
                        "Build scalable backend services, develop REST APIs, optimize database queries, and collaborate with frontend teams to deliver production-ready applications.",
                        "Full Time"),
                buildJobDetail(
                        "Full Stack Developer",
                        "BlueWave Digital",
                        "Hyderabad, India",
                        "INR 8-13 LPA",
                        "Java, Spring Boot, HTML, CSS, JavaScript, MySQL",
                        "Design and maintain complete web applications across frontend and backend layers while improving performance, usability, and deployment quality.",
                        "Full Time"),
                buildJobDetail(
                        "Python Developer",
                        "CodeBridge Labs",
                        "Pune, India",
                        "INR 7-12 LPA",
                        "Python, Django, Flask, APIs, SQL",
                        "Develop Python-based applications, integrate third-party services, write clean business logic, and support production issue resolution.",
                        "Full Time"),
                buildJobDetail(
                        "Data Scientist",
                        "InsightForge Analytics",
                        "Chennai, India",
                        "INR 10-18 LPA",
                        "Python, Pandas, NumPy, Machine Learning, SQL, Data Visualization",
                        "Analyze datasets, build predictive models, prepare features, and communicate actionable insights to business and engineering stakeholders.",
                        "Full Time"),
                buildJobDetail(
                        "Cloud Application Developer",
                        "Nimbus Stack",
                        "Remote",
                        "INR 11-17 LPA",
                        "AWS, Docker, Microservices, CI/CD, Spring Boot",
                        "Create cloud-native applications, containerize services, support deployment pipelines, and improve scalability and resilience in distributed systems.",
                        "Full Time"),
                buildJobDetail(
                        "QA Automation Engineer",
                        "AssurePath Systems",
                        "Noida, India",
                        "INR 6-10 LPA",
                        "Selenium, JUnit, Testing, Test Automation, Java",
                        "Build and maintain automation suites, validate business-critical workflows, document defects, and improve release confidence across environments.",
                        "Full Time"),
                buildJobDetail(
                        "Security Engineer",
                        "SecureAxis Technologies",
                        "Gurugram, India",
                        "INR 12-20 LPA",
                        "Cybersecurity, Security, Threat Analysis, Vulnerability Management, Network Security",
                        "Strengthen application and infrastructure security by assessing vulnerabilities, defining safeguards, and supporting secure engineering practices.",
                        "Full Time"),
                buildJobDetail(
                        "Network Security Specialist",
                        "ShieldNet Infra",
                        "Mumbai, India",
                        "INR 9-15 LPA",
                        "Network Security, Cybersecurity, Firewalls, Incident Response, Monitoring",
                        "Protect enterprise networks through monitoring, policy enforcement, incident handling, and continual improvement of security controls.",
                        "Full Time"),
                buildJobDetail(
                        "Software Engineer",
                        "Vertex Apps",
                        "Bengaluru, India",
                        "INR 7-11 LPA",
                        "Java, Python, Problem Solving, SQL, APIs",
                        "Contribute to application development, fix defects, implement features, and collaborate with product and QA teams on reliable software delivery.",
                        "Full Time"));

        for (JobDetail jobDetail : defaults) {
            if (!jobDetailRepository.existsByJobRole(jobDetail.getJobRole())) {
                jobDetailRepository.save(jobDetail);
            }
        }
    }

    private JobDetail buildJobDetail(String jobRole,
                                     String companyName,
                                     String location,
                                     String salary,
                                     String skillsRequired,
                                     String description,
                                     String employmentType) {
        JobDetail jobDetail = new JobDetail();
        jobDetail.setJobRole(jobRole);
        jobDetail.setCompanyName(companyName);
        jobDetail.setLocation(location);
        jobDetail.setSalary(salary);
        jobDetail.setSkillsRequired(skillsRequired);
        jobDetail.setDescription(description);
        jobDetail.setEmploymentType(employmentType);
        return jobDetail;
    }
}
