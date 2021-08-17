package com.example.carrental.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.user.User;
import com.example.carrental.entity.user.UserDocumentStatus;
import com.example.carrental.entity.user.UserDrivingLicense;
import com.example.carrental.entity.user.UserPassport;
import com.example.carrental.entity.user.UserPhone;
import com.example.carrental.entity.user.UserRole;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapperTest {

  private User user;

  @Autowired
  private UserMapper userMapper;

  @BeforeEach
  public void setup() {
    var userPassport = UserPassport.builder().id(1L).status(UserDocumentStatus.CONFIRMED).build();
    var userDrivingLicense = UserDrivingLicense.builder().id(2L)
        .status(UserDocumentStatus.CONFIRMED).build();
    var phone = UserPhone.builder().id(1L).phone("+375111234567").active(true).build();
    var role = UserRole.builder().id(1L).role("USER").roleDescription("User").build();
    user = User.builder().id(1L).email("test@gmail.com").role(role).locked(false)
        .isEmailConfirmed(true).passport(userPassport).drivingLicense(userDrivingLicense).phones(
            Collections.singletonList(phone)).build();
  }

  @Test
  void userToUserDataResponse() {
    var userDataResponse = userMapper.userToUserDataResponse(user);

    assertThat(userDataResponse).isNotNull();
    assertThat(userDataResponse.getId()).isEqualTo(user.getId());
    assertThat(userDataResponse.getEmail()).isEqualTo(user.getEmail());
    assertThat(userDataResponse.getRole()).isEqualTo(user.getRole().getRoleDescription());
    assertThat(userDataResponse.isLocked()).isEqualTo(user.getLocked());
    assertThat(userDataResponse.isEmailConfirmed()).isEqualTo(user.getIsEmailConfirmed());
    assertThat(userDataResponse.getPhones()).isEqualTo(userMapper.getPhones(user.getPhones()));
    assertThat(userDataResponse.getPassportId()).isEqualTo(user.getPassport().getId());
    assertThat(userDataResponse.getPassportStatus())
        .isEqualTo(user.getPassport().getStatus().getStatus());
    assertThat(userDataResponse.getDrivingLicenseId()).isEqualTo(user.getDrivingLicense().getId());
    assertThat(userDataResponse.getDrivingLicenseStatus())
        .isEqualTo(user.getDrivingLicense().getStatus().getStatus());
  }

  @Test
  void userToUserProfileResponse() {
    var userProfileResponse = userMapper.userToUserProfileResponse(user);

    assertThat(userProfileResponse).isNotNull();
    assertThat(userProfileResponse.getId()).isEqualTo(user.getId());
    assertThat(userProfileResponse.getEmail()).isEqualTo(user.getEmail());
    assertThat(userProfileResponse.getPhones())
        .isEqualTo(userMapper.getPhoneNumbers(user.getPhones()));
    assertThat(userProfileResponse.isEmailConfirmed()).isEqualTo(user.getIsEmailConfirmed());
    assertThat(userProfileResponse.getPassportStatus())
        .isEqualTo(user.getPassport().getStatus().toString());
    assertThat(userProfileResponse.getDrivingLicenseStatus())
        .isEqualTo(user.getDrivingLicense().getStatus().toString());
  }

  @Test
  void getPhones() {
    var phones = userMapper.getPhones(user.getPhones());

    assertThat(phones).isNotNull();
    assertThat(phones.size()).isEqualTo(1);
    assertThat(phones.get(0).getPhone()).isEqualTo("+375111234567");
  }

  @Test
  void getPhoneNumbers() {
    var phoneNumbers = userMapper.getPhoneNumbers(user.getPhones());

    assertThat(phoneNumbers).isNotNull();
    assertThat(phoneNumbers.size()).isEqualTo(1);
    assertThat(phoneNumbers.get(0)).isEqualTo("+375111234567");
  }

  @Test
  void userPhoneToUserPhoneResponse() {
    var userPhone = UserPhone.builder().id(1L).phone("+375111234567").active(true).build();
    var userPhoneResponse = userMapper.userPhoneToUserPhoneResponse(userPhone);

    assertThat(userPhoneResponse).isNotNull();
    assertThat(userPhoneResponse.getId()).isEqualTo(userPhone.getId());
    assertThat(userPhoneResponse.getPhone()).isEqualTo(userPhoneResponse.getPhone());
    assertThat(userPhoneResponse.isActive()).isEqualTo(userPhoneResponse.isActive());
  }
}