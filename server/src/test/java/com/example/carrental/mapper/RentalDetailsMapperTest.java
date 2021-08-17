package com.example.carrental.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.location.Location;
import com.example.carrental.entity.rentaldetails.RentalDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RentalDetailsMapperTest {

  @Autowired
  private RentalDetailsMapper rentalDetailsMapper;

  @Test
  void rentalDetailsToRentalDetailsResponse() {
    var location = Location.builder().id(1L).name("name").build();
    var rentalDetails = RentalDetails.builder().phone("+375111234567").location(location).build();
    var rentalDetailsResponse = rentalDetailsMapper
        .rentalDetailsToRentalDetailsResponse(rentalDetails);

    assertThat(rentalDetailsResponse).isNotNull();
    assertThat(rentalDetailsResponse.getPhoneNumber()).isEqualTo(rentalDetails.getPhone());
    assertThat(rentalDetailsResponse.getLocationId())
        .isEqualTo(rentalDetails.getLocation().getId());
    assertThat(rentalDetailsResponse.getLocation())
        .isEqualTo(rentalDetails.getLocation().getName());
  }
}