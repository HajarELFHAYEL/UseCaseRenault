package com.renault.garage.mapper;

import com.renault.garage.dto.VehicleDTO;
import com.renault.garage.entity.Vehicle;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Vehicle et VehicleDTO.
 */
@Component
public class VehicleMapper {

    public VehicleDTO toDTO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .anneeFabrication(vehicle.getAnneeFabrication())
                .typeCarburant(vehicle.getTypeCarburant())
                .garageId(vehicle.getGarage() != null ? vehicle.getGarage().getId() : null)
                .garageName(vehicle.getGarage() != null ? vehicle.getGarage().getName() : null)
                .accessoryCount(vehicle.getAccessories() != null ? vehicle.getAccessories().size() : 0)
                .build();
    }

    public Vehicle toEntity(VehicleDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Vehicle.builder()
                .id(dto.getId())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .anneeFabrication(dto.getAnneeFabrication())
                .typeCarburant(dto.getTypeCarburant())
                .build();
    }

    public void updateEntityFromDTO(VehicleDTO dto, Vehicle vehicle) {
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setAnneeFabrication(dto.getAnneeFabrication());
        vehicle.setTypeCarburant(dto.getTypeCarburant());
    }
}
