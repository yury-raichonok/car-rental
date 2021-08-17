package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.location.Location;
import com.example.carrental.entity.location.LocationTranslation;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LocationMapperTest {

  private Location location;
  private LocationTranslation locationTranslationRu;
  private LocationTranslation locationTranslationBe;

  @Autowired
  private LocationMapper locationMapper;


  @BeforeEach
  public void setup() {
    locationTranslationRu = LocationTranslation.builder().id(1L).name("locationNameRu")
        .language(RUSSIAN).build();
    locationTranslationBe = LocationTranslation.builder().id(2L).name("locationNameBe")
        .language(BELORUSSIAN).build();
    location = Location.builder().id(1L).name("name").coordinateX(1).coordinateY(2)
        .locationTranslations(Arrays.asList(locationTranslationRu, locationTranslationBe)).build();
  }

  @Test
  void locationToLocationWithTranslationsResponse() {
    var locationWithTranslationsResponse = locationMapper
        .locationToLocationWithTranslationsResponse(location);

    assertThat(locationWithTranslationsResponse).isNotNull();
    assertThat(locationWithTranslationsResponse.getId()).isEqualTo(location.getId());
    assertThat(locationWithTranslationsResponse.getNameEn()).isEqualTo(location.getName());
    assertThat(locationWithTranslationsResponse.getNameRu())
        .isEqualTo(locationTranslationRu.getName());
    assertThat(locationWithTranslationsResponse.getNameBe())
        .isEqualTo(locationTranslationBe.getName());
    assertThat(locationWithTranslationsResponse.getCoordinateX())
        .isEqualTo(location.getCoordinateX());
    assertThat(locationWithTranslationsResponse.getCoordinateY())
        .isEqualTo(location.getCoordinateY());
    assertThat(locationWithTranslationsResponse.getZoom()).isEqualTo(location.getZoom());
  }

  @Test
  void locationToLocationNameResponse() {
    var locationNameResponse = locationMapper.locationToLocationNameResponse(location);

    assertThat(locationNameResponse).isNotNull();
    assertThat(locationNameResponse.getId()).isEqualTo(location.getId());
    assertThat(locationNameResponse.getName()).isEqualTo(location.getName());
  }

  @Test
  void getNameRu() {
    var locationNameRu = locationMapper.getNameRu(location);

    assertThat(locationNameRu).isNotNull();
    assertThat(locationNameRu).isEqualTo(locationTranslationRu.getName());
  }

  @Test
  void getNameBe() {
    var locationNameBe = locationMapper.getNameBe(location);

    assertThat(locationNameBe).isNotNull();
    assertThat(locationNameBe).isEqualTo(locationTranslationBe.getName());
  }
}