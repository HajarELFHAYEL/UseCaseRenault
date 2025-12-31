package com.renault.garage.service;

import com.renault.garage.dto.VehicleDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.FuelType;
import com.renault.garage.exception.GarageCapacityExceededException;
import com.renault.garage.exception.GarageNotFoundException;
import com.renault.garage.exception.VehicleNotFoundException;
import com.renault.garage.kafka.VehiclePublisher;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour VehicleService.
 */
@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private VehiclePublisher vehiclePublisher;

    @InjectMocks
    private VehicleService vehicleService;

    private Garage garage;
    private Vehicle vehicle;
    private VehicleDTO vehicleDTO;

    @BeforeEach
    void setUp() {
        garage = Garage.builder()
                .id(1L)
                .name("Garage Paris")
                .address("123 Rue de Paris")
                .telephone("0123456789")
                .email("paris@renault.fr")
                .vehicles(new ArrayList<>())
                .build();

        vehicle = Vehicle.builder()
                .id(1L)
                .brand("Renault")
                .model("Clio")
                .anneeFabrication(2023)
                .typeCarburant(FuelType.ESSENCE)
                .garage(garage)
                .accessories(new ArrayList<>())
                .build();

        vehicleDTO = VehicleDTO.builder()
                .id(1L)
                .brand("Renault")
                .model("Clio")
                .anneeFabrication(2023)
                .typeCarburant(FuelType.ESSENCE)
                .garageId(1L)
                .garageName("Garage Paris")
                .accessoryCount(0)
                .build();
    }

    @Test
    @DisplayName("Ajout d'un véhicule à un garage avec succès")
    void addVehicleToGarage_Success() {
        // Given
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(vehicleRepository.countByGarageId(1L)).thenReturn(0L);
        when(vehicleMapper.toEntity(any(VehicleDTO.class))).thenReturn(vehicle);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(vehicleMapper.toDTO(any(Vehicle.class))).thenReturn(vehicleDTO);

        // When
        VehicleDTO result = vehicleService.addVehicleToGarage(1L, vehicleDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBrand()).isEqualTo("Renault");
        assertThat(result.getModel()).isEqualTo("Clio");
        verify(vehiclePublisher).publishVehicleCreated(any(Vehicle.class));
    }

    @Test
    @DisplayName("Ajout d'un véhicule à un garage inexistant lève une exception")
    void addVehicleToGarage_GarageNotFound() {
        // Given
        when(garageRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> vehicleService.addVehicleToGarage(999L, vehicleDTO))
                .isInstanceOf(GarageNotFoundException.class);
    }

    @Test
    @DisplayName("Ajout d'un véhicule à un garage plein lève une exception")
    void addVehicleToGarage_CapacityExceeded() {
        // Given - Mock le count à 50 véhicules
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(vehicleRepository.countByGarageId(1L)).thenReturn(50L);

        // When/Then
        assertThatThrownBy(() -> vehicleService.addVehicleToGarage(1L, vehicleDTO))
                .isInstanceOf(GarageCapacityExceededException.class)
                .hasMessageContaining("50 véhicules");
    }

    @Test
    @DisplayName("Récupération d'un véhicule par ID avec succès")
    void getVehicleById_Success() {
        // Given
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toDTO(vehicle)).thenReturn(vehicleDTO);

        // When
        VehicleDTO result = vehicleService.getVehicleById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Récupération d'un véhicule inexistant lève une exception")
    void getVehicleById_NotFound() {
        // Given
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> vehicleService.getVehicleById(999L))
                .isInstanceOf(VehicleNotFoundException.class);
    }

    @Test
    @DisplayName("Liste des véhicules d'un garage")
    void getVehiclesByGarageId_Success() {
        // Given
        when(garageRepository.existsById(1L)).thenReturn(true);
        when(vehicleRepository.findByGarageId(1L)).thenReturn(List.of(vehicle));
        when(vehicleMapper.toDTO(any(Vehicle.class))).thenReturn(vehicleDTO);

        // When
        List<VehicleDTO> result = vehicleService.getVehiclesByGarageId(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getModel()).isEqualTo("Clio");
    }

    @Test
    @DisplayName("Recherche de véhicules par modèle")
    void getVehiclesByModel_Success() {
        // Given
        when(vehicleRepository.findByModelContaining("Clio")).thenReturn(List.of(vehicle));
        when(vehicleMapper.toDTO(any(Vehicle.class))).thenReturn(vehicleDTO);

        // When
        List<VehicleDTO> result = vehicleService.getVehiclesByModel("Clio");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getModel()).isEqualTo("Clio");
    }

    @Test
    @DisplayName("Suppression d'un véhicule avec succès")
    void deleteVehicle_Success() {
        // Given
        vehicle.setGarage(garage);
        garage.getVehicles().add(vehicle);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        // When
        vehicleService.deleteVehicle(1L);

        // Then
        verify(vehicleRepository).delete(vehicle);
    }
}
