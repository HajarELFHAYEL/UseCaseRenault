package com.renault.garage.service;

import com.renault.garage.dto.GarageDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.exception.GarageNotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.repository.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour GarageService.
 */
@ExtendWith(MockitoExtension.class)
class GarageServiceTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private GarageMapper garageMapper;

    @InjectMocks
    private GarageService garageService;

    private Garage garage;
    private GarageDTO garageDTO;

    @BeforeEach
    void setUp() {
        garage = Garage.builder()
                .id(1L)
                .name("Garage Paris")
                .address("123 Rue de Paris")
                .telephone("0123456789")
                .email("paris@renault.fr")
                .build();

        garageDTO = GarageDTO.builder()
                .id(1L)
                .name("Garage Paris")
                .address("123 Rue de Paris")
                .telephone("0123456789")
                .email("paris@renault.fr")
                .vehicleCount(0)
                .build();
    }

    @Test
    @DisplayName("Création d'un garage avec succès")
    void createGarage_Success() {
        // Given
        when(garageMapper.toEntity(any(GarageDTO.class))).thenReturn(garage);
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);
        when(garageMapper.toDTO(any(Garage.class))).thenReturn(garageDTO);

        // When
        GarageDTO result = garageService.createGarage(garageDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Garage Paris");
        verify(garageRepository).save(any(Garage.class));
    }

    @Test
    @DisplayName("Récupération d'un garage par ID avec succès")
    void getGarageById_Success() {
        // Given
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(garageMapper.toDTO(garage)).thenReturn(garageDTO);

        // When
        GarageDTO result = garageService.getGarageById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Récupération d'un garage inexistant lève une exception")
    void getGarageById_NotFound() {
        // Given
        when(garageRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> garageService.getGarageById(999L))
                .isInstanceOf(GarageNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("Liste paginée des garages")
    void getAllGarages_Paginated() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Garage> garagePage = new PageImpl<>(List.of(garage));
        
        when(garageRepository.findAll(pageable)).thenReturn(garagePage);
        when(garageMapper.toDTO(any(Garage.class))).thenReturn(garageDTO);

        // When
        Page<GarageDTO> result = garageService.getAllGarages(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Garage Paris");
    }

    @Test
    @DisplayName("Mise à jour d'un garage avec succès")
    void updateGarage_Success() {
        // Given
        GarageDTO updateDTO = GarageDTO.builder()
                .name("Garage Paris Updated")
                .address("456 Avenue de Paris")
                .telephone("0987654321")
                .email("paris.updated@renault.fr")
                .build();

        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);
        when(garageMapper.toDTO(any(Garage.class))).thenReturn(updateDTO);

        // When
        GarageDTO result = garageService.updateGarage(1L, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(garageMapper).updateEntityFromDTO(updateDTO, garage);
        verify(garageRepository).save(garage);
    }

    @Test
    @DisplayName("Suppression d'un garage avec succès")
    void deleteGarage_Success() {
        // Given
        when(garageRepository.existsById(1L)).thenReturn(true);

        // When
        garageService.deleteGarage(1L);

        // Then
        verify(garageRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Suppression d'un garage inexistant lève une exception")
    void deleteGarage_NotFound() {
        // Given
        when(garageRepository.existsById(999L)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> garageService.deleteGarage(999L))
                .isInstanceOf(GarageNotFoundException.class);
    }
}
