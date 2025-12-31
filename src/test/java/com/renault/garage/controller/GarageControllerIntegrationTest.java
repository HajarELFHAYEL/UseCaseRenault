package com.renault.garage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.dto.GarageDTO;
import com.renault.garage.dto.OpeningHoursDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.GarageOpeningHours;
import com.renault.garage.repository.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour GarageController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class GarageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GarageRepository garageRepository;

    private Garage testGarage;

    @BeforeEach
    void setUp() {
        garageRepository.deleteAll();
        
        testGarage = Garage.builder()
                .name("Garage Test Paris")
                .address("123 Rue de Test")
                .telephone("0123456789")
                .email("test@renault.fr")
                .build();
        
        GarageOpeningHours mondayMorning = GarageOpeningHours.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(12, 0))
                .build();
        testGarage.addOpeningHours(mondayMorning);
        
        testGarage = garageRepository.save(testGarage);
    }

    @Test
    @DisplayName("POST /api/garages - Création d'un garage avec succès")
    void createGarage_Success() throws Exception {
        GarageDTO newGarage = GarageDTO.builder()
                .name("Nouveau Garage")
                .address("456 Avenue de la Création")
                .telephone("0987654321")
                .email("nouveau@renault.fr")
                .build();

        mockMvc.perform(post("/api/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGarage)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Nouveau Garage"))
                .andExpect(jsonPath("$.email").value("nouveau@renault.fr"));
    }

    @Test
    @DisplayName("POST /api/garages - Erreur de validation si champs manquants")
    void createGarage_ValidationError() throws Exception {
        GarageDTO invalidGarage = GarageDTO.builder()
                .name("") // Nom vide
                .build();

        mockMvc.perform(post("/api/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidGarage)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @DisplayName("GET /api/garages/{id} - Récupération d'un garage")
    void getGarageById_Success() throws Exception {
        mockMvc.perform(get("/api/garages/{id}", testGarage.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testGarage.getId()))
                .andExpect(jsonPath("$.name").value("Garage Test Paris"))
                .andExpect(jsonPath("$.vehicleCount").value(0));
    }

    @Test
    @DisplayName("GET /api/garages/{id} - Garage non trouvé")
    void getGarageById_NotFound() throws Exception {
        mockMvc.perform(get("/api/garages/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("99999")));
    }

    @Test
    @DisplayName("GET /api/garages - Liste paginée des garages")
    void getAllGarages_Paginated() throws Exception {
        // Ajouter plus de garages pour tester la pagination
        for (int i = 1; i <= 15; i++) {
            garageRepository.save(Garage.builder()
                    .name("Garage " + i)
                    .address("Adresse " + i)
                    .telephone("01234567" + String.format("%02d", i))
                    .email("garage" + i + "@renault.fr")
                    .build());
        }

        mockMvc.perform(get("/api/garages")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.totalElements").value(16)) // 1 de setUp + 15 ajoutés
                .andExpect(jsonPath("$.totalPages").value(4));
    }

    @Test
    @DisplayName("PUT /api/garages/{id} - Mise à jour d'un garage")
    void updateGarage_Success() throws Exception {
        GarageDTO updateDTO = GarageDTO.builder()
                .name("Garage Modifié")
                .address("789 Boulevard Modifié")
                .telephone("0111111111")
                .email("modifie@renault.fr")
                .build();

        mockMvc.perform(put("/api/garages/{id}", testGarage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Garage Modifié"))
                .andExpect(jsonPath("$.address").value("789 Boulevard Modifié"));
    }

    @Test
    @DisplayName("DELETE /api/garages/{id} - Suppression d'un garage")
    void deleteGarage_Success() throws Exception {
        mockMvc.perform(delete("/api/garages/{id}", testGarage.getId()))
                .andExpect(status().isNoContent());

        // Vérifier que le garage n'existe plus
        mockMvc.perform(get("/api/garages/{id}", testGarage.getId()))
                .andExpect(status().isNotFound());
    }
}
