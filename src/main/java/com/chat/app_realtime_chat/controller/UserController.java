package com.chat.app_realtime_chat.controller;

import com.chat.app_realtime_chat.model.User;
import com.chat.app_realtime_chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // NEW: Endpoint to get the currently authenticated Google User
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        // Google provides 'name' and 'picture' attributes
        String rawName = principal.getAttribute("name");
        String username = rawName.replaceAll("\\s+", ""); // Remove spaces for our chat ID
        String picture = principal.getAttribute("picture");

        // Auto-register them using their real Google Avatar if they are new
        if (!userRepository.existsById(username)) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setAvatarUrl(picture != null ? picture : "https://api.dicebear.com/7.x/pixel-art/svg?seed=" + username);
            newUser.setThemeColor("#0084ff");
            userRepository.save(newUser);
        }

        return ResponseEntity.ok(Map.of("username", username, "avatarUrl", picture != null ? picture : ""));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserProfile(@PathVariable String username) {
        return userRepository.findById(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{username}")
    public ResponseEntity<User> updateUserProfile(@PathVariable String username, @RequestBody User updatedUser) {
        User user = userRepository.findById(username).orElse(new User());
        user.setUsername(username);
        user.setAvatarUrl(updatedUser.getAvatarUrl());
        user.setThemeColor(updatedUser.getThemeColor());

        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}