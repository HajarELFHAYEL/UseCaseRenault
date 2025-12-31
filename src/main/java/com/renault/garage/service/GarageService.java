package com.renault.garage.service;

import com.renault.garage.dto.GarageDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.enums.FuelType;
import com.renault.garage.exception.GarageNotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.repository.GarageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des garages.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;

    /**
     * Crée un nouveau garage.
     */
    public GarageDTO createGarage(GarageDTO garageDTO) {
        log.info("Création d'un nouveau garage: {}", garageDTO.getName());
        Garage garage = garageMapper.toEntity(garageDTO);
        Garage savedGarage = garageRepository.save(garage);
        return garageMapper.toDTO(savedGarage);
    }

    /**
     * Récupère un garage par son ID.
     */
    @Transactional(readOnly = true)
    public GarageDTO getGarageById(Long id) {
        log.info("Recherche du garage avec l'ID: {}", id);
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new GarageNotFoundException(id));
        return garageMapper.toDTO(garage);
    }

    /**
     * Récupère tous les garages avec pagination et tri.
     */
    @Transactional(readOnly = true)
    public Page<GarageDTO> getAllGarages(Pageable pageable) {
        log.info("Récupération de tous les garages - Page: {}, Taille: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        return garageRepository.findAll(pageable)
                .map(garageMapper::toDTO);
    }

    /**
     * Met à jour un garage existant.
     */
    public GarageDTO updateGarage(Long id, GarageDTO garageDTO) {
        log.info("Mise à jour du garage avec l'ID: {}", id);
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new GarageNotFoundException(id));
        
        garageMapper.updateEntityFromDTO(garageDTO, garage);
        Garage updatedGarage = garageRepository.save(garage);
        return garageMapper.toDTO(updatedGarage);
    }

    /**
     * Supprime un garage.
     */
    public void deleteGarage(Long id) {
        log.info("Suppression du garage avec l'ID: {}", id);
        if (!garageRepository.existsById(id)) {
            throw new GarageNotFoundException(id);
        }
        garageRepository.deleteById(id);
    }

    /**
     * Recherche les garages par type de carburant des véhicules.
     */
    @Transactional(readOnly = true)
    public List<GarageDTO> findByVehicleFuelType(FuelType fuelType) {
        log.info("Recherche des garages avec véhicules de type: {}", fuelType);
        return garageRepository.findByVehicleFuelType(fuelType)
                .stream()
                .map(garageMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Recherche les garages contenant un accessoire spécifique.
     */
    @Transactional(readOnly = true)
    public List<GarageDTO> findByAccessoryName(String accessoryName) {
        log.info("Recherche des garages avec l'accessoire: {}", accessoryName);
        return garageRepository.findByAccessoryName(accessoryName)
                .stream()
                .map(garageMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère l'entité Garage (usage interne).
     */
    @Transactional(readOnly = true)
    public Garage getGarageEntityById(Long id) {
        return garageRepository.findById(id)
                .orElseThrow(() -> new GarageNotFoundException(id));
    }
}
