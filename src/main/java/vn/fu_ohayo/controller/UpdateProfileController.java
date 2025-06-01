//package vn.fu_ohayo.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import vn.fu_ohayo.entity.User;
//import vn.fu_ohayo.entity.UserProfileUpdateDTO;
//import vn.fu_ohayo.repository.UserRepository;
//
//@AllArgsConstructor
//@RestController
//@RequestMapping("/api/user")
//public class UpdateProfileController {
//
//    private final UserRepository userRepository;
//
//    @PostMapping("/update-profile")
//    public ResponseEntity<?> updateProfile(UserProfileUpdateDTO request) {
//        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
//        user.setFullName(request.getName());
//        user.setPhone(request.getPhoneNumber());
//        user.setAddress(request.getAddress());
//        userRepository.save(user);
//        return ResponseEntity.ok("Profile updated successfully");
//    }
//}