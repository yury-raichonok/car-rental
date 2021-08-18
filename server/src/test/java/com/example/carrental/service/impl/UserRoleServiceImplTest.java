package com.example.carrental.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.carrental.entity.user.UserRole;
import com.example.carrental.repository.UserRoleRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
class UserRoleServiceImplTest {

  @Autowired
  private UserRoleServiceImpl userRoleService;

  @MockBean
  private UserRoleRepository userRoleRepository;

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    var role = UserRole.builder().id(1L).role("USER").roleDescription("User").build();
    when(userRoleRepository.findById(1L)).thenReturn(Optional.of(role));

    var userRole = userRoleService.findById(1L);

    assertThat(userRole).isNotNull();
    assertThat(userRole.getId()).isEqualTo(role.getId());
    assertThat(userRole.getRole()).isEqualTo(role.getRole());
    assertThat(userRole.getRoleDescription()).isEqualTo(role.getRoleDescription());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(userRoleRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> userRoleService.findById(1L));
  }

  @Test
  void givenValidRequest_whenFindByRole_thenSuccess() {
    var role = UserRole.builder().id(1L).role("USER").roleDescription("User").build();
    when(userRoleRepository.findByRole("USER")).thenReturn(Optional.of(role));

    var userRole = userRoleService.findByRole("USER");

    assertThat(userRole).isNotNull();
    assertThat(userRole.getId()).isEqualTo(role.getId());
    assertThat(userRole.getRole()).isEqualTo(role.getRole());
    assertThat(userRole.getRoleDescription()).isEqualTo(role.getRoleDescription());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindByRole_thenThrowIllegalStateException() {
    when(userRoleRepository.findByRole("USER")).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> userRoleService.findByRole("USER"));
  }
}