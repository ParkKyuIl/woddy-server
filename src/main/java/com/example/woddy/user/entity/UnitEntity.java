package com.example.woddy.user.entity;

import com.example.woddy.user.dto.UnitRequestDTO;
import com.example.woddy.user.enums.MeasurementType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Table(name = "units")
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class UnitEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private MeasurementType measurmentType;

  private long value;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(nullable = false)
  private WodDetailEntity wodDetail;

  public static UnitEntity parseDTOtoEntity(UnitRequestDTO unitRequestDTO) {

    return UnitEntity.builder().measurmentType(unitRequestDTO.getMeasurmentType())
        .value(unitRequestDTO.getValue()).build();
  }
}
