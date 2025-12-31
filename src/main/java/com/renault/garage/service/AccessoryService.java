package com.renault.garage.service;

import com.renault.garage.dto.AccessoryDTO;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.exception.AccessoryNotFoundException;
import com.renault.garage.exception.VehicleNotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des accessoires.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final VehicleRepository vehicleRepository;
    private final AccessoryMapper accessoryMapper;

    /**
     * Ajoute un accessoire à un véhicule.
     */
    public AccessoryDTO addAccessoryToVehicle(Long vehicleId, AccessoryDTO accessoryDTO) {
        log.info("Ajout d'un accessoire au véhicule {}: {}", vehicleId, accessoryDTO.getNom());
        
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException(vehicleId));
        
        Accessory accessory = accessoryMapper.toEntity(accessoryDTO);
        vehicle.addAccessory(accessory);
        
        Accessory savedAccessory = accessoryRepository.save(accessory);
        return accessoryMapper.toDTO(savedAccessory);
    }

    /**
     * Récupère un accessoire par son ID.
     */
    @Transactional(readOnly = true)
    public AccessoryDTO getAccessoryById(Long id) {
        log.info("Recherche de l'accessoire avec l'ID: {}", id);
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new AccessoryNotFoundException(id));
        return accessoryMapper.toDTO(accessory);
    }

    /**
     * Récupère tous les accessoires d'un véhicule.
     */
    @Transactional(readOnly = true)
    public List<AccessoryDTO> getAccessoriesByVehicleId(Long vehicleId) {
        log.info("Récupération des accessoires du véhicule: {}", vehicleId);
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new VehicleNotFoundException(vehicleId);
        }
        return accessoryRepository.findByVehicleId(vehicleId)
                .stream()
                .map(accessoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour un accessoire.
     */
    public AccessoryDTO updateAccessory(Long id, AccessoryDTO accessoryDTO) {
        log.info("Mise à jour de l'accessoire avec l'ID: {}", id);
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new AccessoryNotFoundException(id));
        
        accessoryMapper.updateEntityFromDTO(accessoryDTO, accessory);
        Accessory updatedAccessory = accessoryRepository.save(accessory);
        return accessoryMapper.toDTO(updatedAccessory);
    }

    /**
     * Supprime un accessoire.
     */
    public void deleteAccessory(Long id) {
        log.info("Suppression de l'accessoire avec l'ID: {}", id);
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new AccessoryNotFoundException(id));
        
        if (accessory.getVehicle() != null) {
            accessory.getVehicle().removeAccessory(accessory);
        }
        accessoryRepository.delete(accessory);
    }
}
