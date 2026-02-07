package com.assessmentprod.userServiceV1;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.assessmentprod.userServiceV1.controller.AdminController;
import com.assessmentprod.userServiceV1.dto.UserInfoRes;
import com.assessmentprod.userServiceV1.entity.role;
import com.assessmentprod.userServiceV1.exception.UserNotFoundException;
import com.assessmentprod.userServiceV1.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        // Arrange
        List<UserInfoRes> userList = List.of(
                new UserInfoRes(1L, "User1", "user1@example.com", role.USER),
                new UserInfoRes(2L, "User2", "user2@example.com", role.ADMIN)
        );
        when(adminService.getAllUsers()).thenReturn(userList);

        // Act and Assert
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userList)));
    }

    @Test
    public void getUser_WhenUserExists_ShouldReturnUser() throws Exception {
        // Arrange
        Long userId = 1L;
        UserInfoRes user = new UserInfoRes(userId, "User1", "user1@example.com", role.USER);
        when(adminService.getUser(userId)).thenReturn(user);

        // Act and Assert
        mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

//    @Test
//    public void getUser_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
//        // Arrange
//        Long userId = 1L;
//        when(adminService.getUser(userId)).thenThrow(new UserNotFoundException(userId));
//
//        // Act and Assert
//        mockMvc.perform(get("/users/{id}", userId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("User not found"));
//    }

    @Test
    public void updateUserRole_ShouldReturnUpdatedUser() throws Exception {
        // Arrange
        Long userId = 1L;
        role newRole = role.ADMIN;
        UserInfoRes updatedUser = new UserInfoRes(userId, "User1", "user1@example.com", newRole);
        when(adminService.updateUserRole(userId, newRole)).thenReturn(updatedUser);

        // Act and Assert
        mockMvc.perform(put("/users/{id}/role", userId)
                        .param("updateTo", newRole.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)));
    }
}

