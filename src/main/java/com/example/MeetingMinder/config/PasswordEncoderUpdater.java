//package com.example.MeetingMinder.config;
//
//import com.example.MeetingMinder.model.User;
//import com.example.MeetingMinder.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//
//@Component
//public class PasswordEncoderUpdater {
//
//    private final UserRepository userRepository;
//
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    @Value("${password.encoder.updater.enabled:true}")
//    private boolean isEnabled;
//
//    public PasswordEncoderUpdater(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
//        this.passwordEncoder = passwordEncoder;
//        this.userRepository = userRepository;
//    }
//
//    @PostConstruct
//    public void encodeExistingPasswords() {
//        if (isEnabled) {
//            List<User> users = userRepository.findAll();
//            for (User user : users) {
//                if (!user.getPassword().startsWith("$2a$")) {
//                    user.setPassword(passwordEncoder.encode(user.getPassword()));
//                    userRepository.save(user);
//                }
//            }
//        }
//    }
//}
