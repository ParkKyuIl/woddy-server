package com.example.woddy.user.entity;

import com.example.woddy.user.dto.WodDetailRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "wod_details")
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WodDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 와드 상세 정보 아이디 (PK)

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="wod_id", referencedColumnName = "id", nullable = false)
    private WodEntity wod; // 와드 정보 아이디 (FK)

    @OneToOne
    @JoinColumn(name="workout_dictionary_id")
    private WorkoutDictionaryEntity workoutDictionaryEntity;

    @OneToMany(mappedBy = "wodDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnitEntity> records;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 생성된 날짜

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 업데이트된 날짜

    public WodDetailEntity(long id) {
        this.id = id;
    }

    public static WodDetailEntity parseDtoToEntity(WodDetailRequestDTO requestDto) {
        WorkoutDictionaryEntity workoutDictionaryEntity = new WorkoutDictionaryEntity(requestDto.getWorkoutId());
        List<UnitEntity> unitEntities = requestDto.getRecords().stream().map(UnitEntity::parseDTOtoEntity).collect(
            Collectors.toList());

        return WodDetailEntity.builder().workoutDictionaryEntity(workoutDictionaryEntity).records(unitEntities).build();
    }
}
