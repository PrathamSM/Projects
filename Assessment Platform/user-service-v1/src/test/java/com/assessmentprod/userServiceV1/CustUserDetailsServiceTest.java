package com.assessmentprod.userServiceV1;

import com.assessmentprod.userServiceV1.dto.UserUpdateReq;
import com.assessmentprod.userServiceV1.entity.UserData;
import com.assessmentprod.userServiceV1.entity.role;
import com.assessmentprod.userServiceV1.repository.UserDataRepository;
import com.assessmentprod.userServiceV1.service.CustUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustUserDetailsServiceTest {

    @Mock
    private UserDataRepository userDataRepository;

    @InjectMocks
    private CustUserDetailsService custUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateProfile_WhenUserExists_ShouldUpdateAndReturnSuccessMessage() {
        // Arrange
        Long userId = 1L;
        UserUpdateReq updateReq = new UserUpdateReq("UpdatedUser", "updated@example.com");
        UserData existingUser = new UserData(userId, "OriginalUser", "original@example.com", role.USER);

        when(userDataRepository.existsById(userId)).thenReturn(true);
        when(userDataRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userDataRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        ResponseEntity<String> response = custUserDetailsService.updateProfile(userId, updateReq);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK");
        assertEquals("User Updated Successfully!", response.getBody(), "Response message should match");
        assertEquals("UpdatedUser", existingUser.getUsername(), "Username should be updated");
        assertEquals("updated@example.com", existingUser.getEmail(), "Email should be updated");

        // Verify interactions
        verify(userDataRepository).existsById(userId);
        verify(userDataRepository).findById(userId);
        verify(userDataRepository).save(existingUser);
    }

    @Test
    void updateProfile_WhenUserDoesNotExist_ShouldReturnBadRequest() {
        // Arrange
        Long userId = 1L;
        UserUpdateReq updateReq = new UserUpdateReq("NonExistentUser", "nonexistent@example.com");

        when(userDataRepository.existsById(userId)).thenReturn(false);

        // Act
        ResponseEntity<String> response = custUserDetailsService.updateProfile(userId, updateReq);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD_REQUEST");
        assertEquals("User doesn't exist", response.getBody(), "Response message should indicate non-existence");

        // Verify interactions
        verify(userDataRepository).existsById(userId);
        verify(userDataRepository, never()).findById(anyLong());
        verify(userDataRepository, never()).save(any());
    }
}
