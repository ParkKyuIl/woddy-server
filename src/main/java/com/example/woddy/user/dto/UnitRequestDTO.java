package com.example.woddy.user.dto;

import com.example.woddy.user.enums.MeasurementType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitRequestDTO {
  @NotEmpty
  private MeasurementType measurmentType;

  @NotEmpty
  private long value;
}
