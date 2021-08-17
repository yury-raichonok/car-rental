package com.example.carrental.controller.dto.car;

import com.example.carrental.entity.car.CarBodyType;
import com.example.carrental.entity.car.CarEngineType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for Cars search request.
 *
 * @author Yury Raichonak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarSearchRequest {

  @NotNull
  @Min(0)
  private int pageNumber = 0;
  @NotNull
  @Min(10)
  private int pageSize = 10;
  @NotNull
  @Size(
      min = 3,
      max = 4
  )
  private String sortDirection = "asc";
  @NotNull
  private String sortBy = "costPerHour";
  private String brandName;
  private String modelName;
  private Long location;
  private Long carClass;
  private String vin;
  private String costFrom;
  private String costTo;
  private String yearFrom;
  private String yearTo;
  private CarBodyType bodyType;
  private CarEngineType engineType;
  private boolean airConditioner;
  private boolean autoTransmission;
  private boolean inRental;
}
