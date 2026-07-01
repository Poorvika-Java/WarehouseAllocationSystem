package com.warehouse.mapper;

import org.springframework.stereotype.Component;

import com.warehouse.entity.Warehouse;
import com.warehouse.requestdto.WarehouseRequest;
import com.warehouse.responsedto.WarehouseResponse;

@Component
public class WarehouseMapper {

	public Warehouse toEntity(WarehouseRequest request) {
	    return Warehouse.builder()
	            .name(request.getName())
	            .location(request.getLocation())
	            .capacity(request.getCapacity())
	            .status(request.getStatus())
	            .build();
	

    }

	public WarehouseResponse toResponse(Warehouse warehouse) {
	    if (warehouse == null) return null;

	    return WarehouseResponse.builder()
	            .id(warehouse.getId())
	            .name(warehouse.getName())
	            .location(warehouse.getLocation())
	            .capacity(warehouse.getCapacity())
	            .status(warehouse.getStatus())
	            .createdAt(warehouse.getCreatedAt())
	            .updatedAt(warehouse.getUpdatedAt())
	            .build();
	}
}