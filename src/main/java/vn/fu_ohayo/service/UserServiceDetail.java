package vn.fu_ohayo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceDetail {
    private final UserRepository userRepository;
    public UserDetailsService userDetailsService() {
        return userRepository::findByEmail;
    }
}
