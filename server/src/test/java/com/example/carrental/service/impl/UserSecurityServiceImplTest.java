package com.example.carrental.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.carrental.entity.user.User;
import com.example.carrental.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
class UserSecurityServiceImplTest {

  @Autowired
  private UserSecurityServiceImpl userSecurityService;

  @MockBean
  private UserRepository userRepository;

  @Test
  void givenValidRequest_whenLoadUserByUsername_thenSuccess() {
    var user = User.builder().id(1L).email("email@gmail.com").build();
    when(userRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(user));

    var userDetails = userSecurityService.loadUserByUsername("email@gmail.com");

    assertThat(userDetails).isNotNull();
    assertThat(userDetails.getUsername()).isEqualTo(user.getEmail());
  }

  @Test
  void givenRequestWithNotExistingId_whenLoadUserByUsername_thenThrowUsernameNotFoundException() {
    when(userRepository.findByEmail("email@gmail.com")).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class,
        () -> userSecurityService.loadUserByUsername("email@gmail.com"));
  }
}