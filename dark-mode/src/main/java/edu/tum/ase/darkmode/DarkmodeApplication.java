package edu.tum.ase.darkmode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/api/dark-mode")
@CrossOrigin(origins = "http://localhost:4200")
public class DarkmodeApplication {

    private boolean darkModeEnabled = false;
    private long lastToggleTime = System.currentTimeMillis();

    public static void main(String[] args) {
        SpringApplication.run(DarkmodeApplication.class, args);
    }

    @GetMapping("/toggle")
    public void toggleDarkMode() {
        long currentTime = System.currentTimeMillis();
        // Add a cooldown of 3 seconds
        if (currentTime - lastToggleTime >= 3000) {
            darkModeEnabled = !darkModeEnabled;
            lastToggleTime = currentTime;
        }
    }

    @GetMapping("/")
    public boolean getCurrentDarkModeStatus() {
        return darkModeEnabled;
    }
}