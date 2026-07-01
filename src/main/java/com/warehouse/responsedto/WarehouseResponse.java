package com.warehouse.responsedto;

import java.time.LocalDateTime;
import com.warehouse.enums.WarehouseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponse {

    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private WarehouseStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}