package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.UserRegister;
import vn.fu_ohayo.dto.response.UserResponse;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.UserService;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImp(UserRepository userRepository, UserMapper userMapper) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse registerUser(UserRegister userRegister) {
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(userRegister)));
    }

}
