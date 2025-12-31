package com.renault.garage.mapper;

import com.renault.garage.dto.AccessoryDTO;
import com.renault.garage.entity.Accessory;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Accessory et AccessoryDTO.
 */
@Component
public class AccessoryMapper {

    public AccessoryDTO toDTO(Accessory accessory) {
        if (accessory == null) {
            return null;
        }
        
        return AccessoryDTO.builder()
                .id(accessory.getId())
                .nom(accessory.getNom())
                .description(accessory.getDescription())
                .prix(accessory.getPrix())
                .type(accessory.getType())
                .vehicleId(accessory.getVehicle() != null ? accessory.getVehicle().getId() : null)
                .build();
    }

    public Accessory toEntity(AccessoryDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Accessory.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .description(dto.getDescription())
                .prix(dto.getPrix())
                .type(dto.getType())
                .build();
    }

    public void updateEntityFromDTO(AccessoryDTO dto, Accessory accessory) {
        accessory.setNom(dto.getNom());
        accessory.setDescription(dto.getDescription());
        accessory.setPrix(dto.getPrix());
        accessory.setType(dto.getType());
    }
}
