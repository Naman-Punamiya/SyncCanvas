package com.ndevstudios.synccanvas.service;

import com.ndevstudios.synccanvas.dto.request.CreateUserRequest;
import com.ndevstudios.synccanvas.dto.response.UserResponse;
import com.ndevstudios.synccanvas.entity.User;
import com.ndevstudios.synccanvas.exception.EmailAlreadyExistsException;
import com.ndevstudios.synccanvas.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream().map(user -> new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getCreatedAt()
        )).toList();
    }

    public Optional<User> getUserByID(Long id){
        return userRepository.findById(id);
    }

    public UserResponse createUser(CreateUserRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        String hashPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getEmail(),
                hashPassword,
                request.getDisplayName()
        );

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getDisplayName(),
                savedUser.getCreatedAt()
        );
    }

//    public User updateUser(Long id,User newUser){
//        User oldUser = userRepository.findById(id);
//        if(oldUser != null){
//            oldUser.setEmail(newUser.getEmail());
//            oldUser.setDisplayName(newUser.getDisplayName());
//        }
//    }

    public User deleteUserByID(Long id){
//        return userRepository.deleteById(id);
        return null;
    }

}
