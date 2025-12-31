package com.renault.garage.service;

import com.renault.garage.dto.AccessoryDTO;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.AccessoryType;
import com.renault.garage.enums.FuelType;
import com.renault.garage.exception.AccessoryNotFoundException;
import com.renault.garage.exception.VehicleNotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour AccessoryService.
 */
@ExtendWith(MockitoExtension.class)
class AccessoryServiceTest {

    @Mock
    private AccessoryRepository accessoryRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AccessoryMapper accessoryMapper;

    @InjectMocks
    private AccessoryService accessoryService;

    private Vehicle vehicle;
    private Accessory accessory;
    private AccessoryDTO accessoryDTO;

    @BeforeEach
    void setUp() {
        vehicle = Vehicle.builder()
                .id(1L)
                .brand("Renault")
                .model("Clio")
                .anneeFabrication(2023)
                .typeCarburant(FuelType.ESSENCE)
                .accessories(new ArrayList<>())
                .build();

        accessory = Accessory.builder()
                .id(1L)
                .nom("GPS Navigation")
                .description("Système de navigation GPS intégré")
                .prix(new BigDecimal("499.99"))
                .type(AccessoryType.MULTIMEDIA)
                .vehicle(vehicle)
                .build();

        accessoryDTO = AccessoryDTO.builder()
                .id(1L)
                .nom("GPS Navigation")
                .description("Système de navigation GPS intégré")
                .prix(new BigDecimal("499.99"))
                .type(AccessoryType.MULTIMEDIA)
                .vehicleId(1L)
                .build();
    }

    @Test
    @DisplayName("Ajout d'un accessoire à un véhicule avec succès")
    void addAccessoryToVehicle_Success() {
        // Given
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(accessoryMapper.toEntity(any(AccessoryDTO.class))).thenReturn(accessory);
        when(accessoryRepository.save(any(Accessory.class))).thenReturn(accessory);
        when(accessoryMapper.toDTO(any(Accessory.class))).thenReturn(accessoryDTO);

        // When
        AccessoryDTO result = accessoryService.addAccessoryToVehicle(1L, accessoryDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("GPS Navigation");
        assertThat(result.getPrix()).isEqualTo(new BigDecimal("499.99"));
    }

    @Test
    @DisplayName("Ajout d'un accessoire à un véhicule inexistant lève une exception")
    void addAccessoryToVehicle_VehicleNotFound() {
        // Given
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> accessoryService.addAccessoryToVehicle(999L, accessoryDTO))
                .isInstanceOf(VehicleNotFoundException.class);
    }

    @Test
    @DisplayName("Récupération d'un accessoire par ID avec succès")
    void getAccessoryById_Success() {
        // Given
        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));
        when(accessoryMapper.toDTO(accessory)).thenReturn(accessoryDTO);

        // When
        AccessoryDTO result = accessoryService.getAccessoryById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Récupération d'un accessoire inexistant lève une exception")
    void getAccessoryById_NotFound() {
        // Given
        when(accessoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> accessoryService.getAccessoryById(999L))
                .isInstanceOf(AccessoryNotFoundException.class);
    }

    @Test
    @DisplayName("Liste des accessoires d'un véhicule")
    void getAccessoriesByVehicleId_Success() {
        // Given
        when(vehicleRepository.existsById(1L)).thenReturn(true);
        when(accessoryRepository.findByVehicleId(1L)).thenReturn(List.of(accessory));
        when(accessoryMapper.toDTO(any(Accessory.class))).thenReturn(accessoryDTO);

        // When
        List<AccessoryDTO> result = accessoryService.getAccessoriesByVehicleId(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("GPS Navigation");
    }

    @Test
    @DisplayName("Mise à jour d'un accessoire avec succès")
    void updateAccessory_Success() {
        // Given
        AccessoryDTO updateDTO = AccessoryDTO.builder()
                .nom("GPS Premium")
                .description("Système de navigation GPS premium")
                .prix(new BigDecimal("699.99"))
                .type(AccessoryType.MULTIMEDIA)
                .build();

        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));
        when(accessoryRepository.save(any(Accessory.class))).thenReturn(accessory);
        when(accessoryMapper.toDTO(any(Accessory.class))).thenReturn(updateDTO);

        // When
        AccessoryDTO result = accessoryService.updateAccessory(1L, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(accessoryMapper).updateEntityFromDTO(updateDTO, accessory);
    }

    @Test
    @DisplayName("Suppression d'un accessoire avec succès")
    void deleteAccessory_Success() {
        // Given
        accessory.setVehicle(vehicle);
        vehicle.getAccessories().add(accessory);
        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));

        // When
        accessoryService.deleteAccessory(1L);

        // Then
        verify(accessoryRepository).delete(accessory);
    }
}
