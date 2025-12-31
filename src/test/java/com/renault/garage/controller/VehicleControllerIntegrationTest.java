package com.renault.garage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.dto.VehicleDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.FuelType;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour VehicleController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class VehicleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private Garage testGarage;
    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
        garageRepository.deleteAll();

        testGarage = Garage.builder()
                .name("Garage Test")
                .address("123 Rue Test")
                .telephone("0123456789")
                .email("test@renault.fr")
                .build();
        testGarage = garageRepository.save(testGarage);

        testVehicle = Vehicle.builder()
                .brand("Renault")
                .model("Clio")
                .anneeFabrication(2023)
                .typeCarburant(FuelType.ESSENCE)
                .garage(testGarage)
                .build();
        testVehicle = vehicleRepository.save(testVehicle);
    }

    @Test
    @DisplayName("POST /api/garages/{garageId}/vehicles - Ajout d'un véhicule")
    void addVehicleToGarage_Success() throws Exception {
        VehicleDTO newVehicle = VehicleDTO.builder()
                .brand("Renault")
                .model("Megane")
                .anneeFabrication(2024)
                .typeCarburant(FuelType.HYBRIDE)
                .build();

        mockMvc.perform(post("/api/garages/{garageId}/vehicles", testGarage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newVehicle)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Renault"))
                .andExpect(jsonPath("$.model").value("Megane"))
                .andExpect(jsonPath("$.garageId").value(testGarage.getId()));
    }

    @Test
    @DisplayName("POST /api/garages/{garageId}/vehicles - Erreur garage plein (50 véhicules)")
    void addVehicleToGarage_CapacityExceeded() throws Exception {
        // Ajouter 49 véhicules (il y en a déjà 1 de setUp)
        for (int i = 0; i < 49; i++) {
            vehicleRepository.save(Vehicle.builder()
                    .brand("Renault")
                    .model("Model" + i)
                    .anneeFabrication(2020)
                    .typeCarburant(FuelType.DIESEL)
                    .garage(testGarage)
                    .build());
        }

        VehicleDTO extraVehicle = VehicleDTO.builder()
                .brand("Renault")
                .model("Captur")
                .anneeFabrication(2024)
                .typeCarburant(FuelType.ELECTRIQUE)
                .build();

        mockMvc.perform(post("/api/garages/{garageId}/vehicles", testGarage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(extraVehicle)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("50 véhicules")));
    }

    @Test
    @DisplayName("GET /api/vehicles/{id} - Récupération d'un véhicule")
    void getVehicleById_Success() throws Exception {
        mockMvc.perform(get("/api/vehicles/{id}", testVehicle.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testVehicle.getId()))
                .andExpect(jsonPath("$.brand").value("Renault"))
                .andExpect(jsonPath("$.model").value("Clio"));
    }

    @Test
    @DisplayName("GET /api/garages/{garageId}/vehicles - Liste des véhicules d'un garage")
    void getVehiclesByGarage_Success() throws Exception {
        mockMvc.perform(get("/api/garages/{garageId}/vehicles", testGarage.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].model").value("Clio"));
    }

    @Test
    @DisplayName("GET /api/vehicles?model=Clio - Recherche par modèle")
    void getVehiclesByModel_Success() throws Exception {
        // Ajouter un autre véhicule Clio dans un autre garage
        Garage secondGarage = garageRepository.save(Garage.builder()
                .name("Second Garage")
                .address("456 Avenue")
                .telephone("0987654321")
                .email("second@renault.fr")
                .build());

        vehicleRepository.save(Vehicle.builder()
                .brand("Renault")
                .model("Clio")
                .anneeFabrication(2022)
                .typeCarburant(FuelType.DIESEL)
                .garage(secondGarage)
                .build());

        mockMvc.perform(get("/api/vehicles")
                        .param("model", "Clio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].model", everyItem(equalTo("Clio"))));
    }

    @Test
    @DisplayName("PUT /api/vehicles/{id} - Mise à jour d'un véhicule")
    void updateVehicle_Success() throws Exception {
        VehicleDTO updateDTO = VehicleDTO.builder()
                .brand("Renault")
                .model("Clio RS")
                .anneeFabrication(2024)
                .typeCarburant(FuelType.ESSENCE)
                .build();

        mockMvc.perform(put("/api/vehicles/{id}", testVehicle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Clio RS"))
                .andExpect(jsonPath("$.anneeFabrication").value(2024));
    }

    @Test
    @DisplayName("DELETE /api/vehicles/{id} - Suppression d'un véhicule")
    void deleteVehicle_Success() throws Exception {
        mockMvc.perform(delete("/api/vehicles/{id}", testVehicle.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/vehicles/{id}", testVehicle.getId()))
                .andExpect(status().isNotFound());
    }
}
