package com.example.carrental.mapper;

import com.example.carrental.controller.dto.user.UserDataResponse;
import com.example.carrental.controller.dto.user.UserPhoneResponse;
import com.example.carrental.controller.dto.user.UserProfileResponse;
import com.example.carrental.entity.user.User;
import com.example.carrental.entity.user.UserPhone;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "id", source = "user.id")
  @Mapping(target = "email", source = "user.email")
  @Mapping(target = "role", source = "user.role.roleDescription")
  @Mapping(target = "locked", source = "user.locked")
  @Mapping(target = "emailConfirmed", source = "user.isEmailConfirmed")
  @Mapping(target = "phones", source = "user.phones", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "getPhones")
  @Mapping(target = "passportId", source = "user.passport.id", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(target = "passportStatus", source = "user.passport.status.status", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(target = "drivingLicenseId", source = "user.drivingLicense.id", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(target = "drivingLicenseStatus", source = "user.drivingLicense.status.status", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  UserDataResponse userToUserDataResponse(User user);

  @Mapping(target = "id", source = "user.id")
  @Mapping(target = "email", source = "user.email")
  @Mapping(target = "phones", source = "user.phones", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, qualifiedByName = "getPhoneNumbers")
  @Mapping(target = "isEmailConfirmed", source = "user.isEmailConfirmed")
  @Mapping(target = "passportStatus", source = "user.passport.status", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(target = "drivingLicenseStatus", source = "user.drivingLicense.status", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  UserProfileResponse userToUserProfileResponse(User user);

  @Named("getPhones")
  default List<UserPhoneResponse> getPhones(List<UserPhone> phones) {
    List<UserPhoneResponse> responses = new ArrayList<>();
    phones.forEach(phone -> responses.add(userPhoneToUserPhoneResponse(phone)));
    return responses;
  }

  @Named("getPhoneNumbers")
  default List<String> getPhoneNumbers(List<UserPhone> phones) {
    List<String> responses = new ArrayList<>();
    phones.forEach(phone -> {
      if (phone.isActive()) {
        responses.add(phone.getPhone());
      }
    });
    return responses;
  }

  @Mapping(target = "id", source = "userPhone.id")
  @Mapping(target = "phone", source = "userPhone.phone")
  @Mapping(target = "active", source = "userPhone.active")
  UserPhoneResponse userPhoneToUserPhoneResponse(UserPhone userPhone);
}
