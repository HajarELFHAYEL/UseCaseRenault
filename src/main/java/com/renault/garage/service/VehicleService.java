package com.renault.garage.service;

import com.renault.garage.dto.VehicleDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.exception.GarageCapacityExceededException;
import com.renault.garage.exception.GarageNotFoundException;
import com.renault.garage.exception.VehicleNotFoundException;
import com.renault.garage.kafka.VehiclePublisher;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des véhicules.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;
    private final VehicleMapper vehicleMapper;
    private final VehiclePublisher vehiclePublisher;

    /**
     * Ajoute un véhicule à un garage.
     * Vérifie la contrainte de 50 véhicules maximum par garage.
     */
    public VehicleDTO addVehicleToGarage(Long garageId, VehicleDTO vehicleDTO) {
        log.info("Ajout d'un véhicule au garage {}: {} {}", 
                garageId, vehicleDTO.getBrand(), vehicleDTO.getModel());
        
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new GarageNotFoundException(garageId));
        
        // Vérification de la contrainte de capacité via le repository (plus fiable)
        long vehicleCount = vehicleRepository.countByGarageId(garageId);
        if (vehicleCount >= Garage.MAX_VEHICLES) {
            throw new GarageCapacityExceededException(garageId);
        }
        
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle.setGarage(garage);
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        
        // Publication de l'événement Kafka
        vehiclePublisher.publishVehicleCreated(savedVehicle);
        
        return vehicleMapper.toDTO(savedVehicle);
    }

    /**
     * Récupère un véhicule par son ID.
     */
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(Long id) {
        log.info("Recherche du véhicule avec l'ID: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        return vehicleMapper.toDTO(vehicle);
    }

    /**
     * Récupère tous les véhicules d'un garage.
     */
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByGarageId(Long garageId) {
        log.info("Récupération des véhicules du garage: {}", garageId);
        if (!garageRepository.existsById(garageId)) {
            throw new GarageNotFoundException(garageId);
        }
        return vehicleRepository.findByGarageId(garageId)
                .stream()
                .map(vehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les véhicules d'un modèle donné.
     */
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByModel(String model) {
        log.info("Recherche des véhicules du modèle: {}", model);
        return vehicleRepository.findByModelContaining(model)
                .stream()
                .map(vehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour un véhicule.
     */
    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        log.info("Mise à jour du véhicule avec l'ID: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        
        vehicleMapper.updateEntityFromDTO(vehicleDTO, vehicle);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDTO(updatedVehicle);
    }

    /**
     * Supprime un véhicule.
     */
    public void deleteVehicle(Long id) {
        log.info("Suppression du véhicule avec l'ID: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        
        if (vehicle.getGarage() != null) {
            vehicle.getGarage().removeVehicle(vehicle);
        }
        vehicleRepository.delete(vehicle);
    }

    /**
     * Récupère l'entité Vehicle (usage interne).
     */
    @Transactional(readOnly = true)
    public Vehicle getVehicleEntityById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }
}
