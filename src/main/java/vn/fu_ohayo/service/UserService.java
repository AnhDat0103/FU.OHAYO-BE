package vn.fu_ohayo.service;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.entity.UserProfileDTO;
import vn.fu_ohayo.repository.UserRepository;

import java.time.format.DateTimeFormatter;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserProfileDTO getUserProfileDTO(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new RuntimeException("User not found"));
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUsername(username);
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setStatus(user.getStatus());
        dto.setMembershipLevel(user.getMembershipLevel());
        dto.setProvider(user.getProvider());
        dto.setProfilePicture(user.getAvatar());
        if (user.getCreatedAt() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dto.setMemberSince("Member since " + user.getCreatedAt().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                    .format(formatter));
        }
        dto.setHasSettingsAccsees(true);
        return dto;
    }
}
