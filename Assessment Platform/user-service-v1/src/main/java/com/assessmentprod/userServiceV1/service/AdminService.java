package com.assessmentprod.userServiceV1.service;

import com.assessmentprod.userServiceV1.dto.UserInfoRes;
import com.assessmentprod.userServiceV1.entity.UserData;
import com.assessmentprod.userServiceV1.entity.role;
import com.assessmentprod.userServiceV1.exception.UserNotFoundException;
import com.assessmentprod.userServiceV1.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private UserDataRepository userDataRepository;

    public List<UserInfoRes> getAllUsers() {
        List<UserData> users = userDataRepository.findByRoleNot(role.ADMIN);
        List<UserInfoRes> userInfos = new ArrayList<>();

        users.stream().forEach(
                userData ->
                userInfos.add(new UserInfoRes(userData.getId(),userData.getUsername(), userData.getEmail(), userData.getRole())
        ));
        return userInfos;
    }


    public UserInfoRes getUser(Long id) {
        Optional<UserData> user = userDataRepository.findById(id);

        if(user.isPresent()) {
            return new UserInfoRes(user.get().getId(), user.get().getUsername(), user.get().getEmail(),user.get().getRole());
        }


        throw new UserNotFoundException(id);
    }


    public UserInfoRes updateUserRole(Long id, role roleToUpdate) {
        Optional<UserData> userOp = userDataRepository.findById(id);

        if(userOp.isPresent()) {
            UserData fetchedUser = userOp.get();
            fetchedUser.setRole(roleToUpdate);
            UserData updatedUser = userDataRepository.save(fetchedUser);
            return new UserInfoRes(updatedUser.getId(),updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getRole());
        }
        else {
//            throw new UsernameNotFoundException("User not found with id : " + id);
          throw new UserNotFoundException(id);
        }
    }

}
