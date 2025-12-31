package com.renault.garage.mapper;

import com.renault.garage.dto.GarageDTO;
import com.renault.garage.dto.OpeningHoursDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.GarageOpeningHours;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre Garage et GarageDTO.
 */
@Component
public class GarageMapper {

    public GarageDTO toDTO(Garage garage) {
        if (garage == null) {
            return null;
        }
        
        return GarageDTO.builder()
                .id(garage.getId())
                .name(garage.getName())
                .address(garage.getAddress())
                .telephone(garage.getTelephone())
                .email(garage.getEmail())
                .horairesOuverture(toOpeningHoursDTOList(garage.getHorairesOuverture()))
                .vehicleCount(garage.getVehicleCount())
                .build();
    }

    public Garage toEntity(GarageDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Garage garage = Garage.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .telephone(dto.getTelephone())
                .email(dto.getEmail())
                .horairesOuverture(new ArrayList<>())
                .build();
        
        if (dto.getHorairesOuverture() != null) {
            dto.getHorairesOuverture().forEach(hoursDto -> {
                GarageOpeningHours hours = toOpeningHoursEntity(hoursDto);
                garage.addOpeningHours(hours);
            });
        }
        
        return garage;
    }

    public void updateEntityFromDTO(GarageDTO dto, Garage garage) {
        garage.setName(dto.getName());
        garage.setAddress(dto.getAddress());
        garage.setTelephone(dto.getTelephone());
        garage.setEmail(dto.getEmail());
        
        if (dto.getHorairesOuverture() != null) {
            garage.getHorairesOuverture().clear();
            dto.getHorairesOuverture().forEach(hoursDto -> {
                GarageOpeningHours hours = toOpeningHoursEntity(hoursDto);
                garage.addOpeningHours(hours);
            });
        }
    }

    private List<OpeningHoursDTO> toOpeningHoursDTOList(List<GarageOpeningHours> hours) {
        if (hours == null) {
            return new ArrayList<>();
        }
        return hours.stream()
                .map(this::toOpeningHoursDTO)
                .collect(Collectors.toList());
    }

    private OpeningHoursDTO toOpeningHoursDTO(GarageOpeningHours hours) {
        return OpeningHoursDTO.builder()
                .id(hours.getId())
                .dayOfWeek(hours.getDayOfWeek())
                .startTime(hours.getStartTime())
                .endTime(hours.getEndTime())
                .build();
    }

    private GarageOpeningHours toOpeningHoursEntity(OpeningHoursDTO dto) {
        return GarageOpeningHours.builder()
                .id(dto.getId())
                .dayOfWeek(dto.getDayOfWeek())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();
    }
}
