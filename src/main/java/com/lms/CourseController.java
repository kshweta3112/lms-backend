package com.lms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CourseController {

    @GetMapping("/api/courses")
    public List<Map<String, Object>> getCourses() {

        return List.of(
            Map.of("id", 1, "name", "Java"),
            Map.of("id", 2, "name", "Spring Boot"),
            Map.of("id", 3, "name", "DevOps")
        );
    }
}
