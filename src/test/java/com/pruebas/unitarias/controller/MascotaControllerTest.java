package com.pruebas.unitarias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebas.unitarias.model.Mascota;
import com.pruebas.unitarias.service.MascotaService;

import org.apache.tomcat.util.http.parser.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MascotaController.class) //lo primero que hay que probar
class MascotaControllerTest {

    @Autowired 
    private MockMvc mockMvc; //es la clase que me va a maquetear lo que necesito para usar esa prueba

    @SuppressWarnings("removal")
    @MockBean //maquetea tambien el servico porque el controlador depende del servicio
    private MascotaService mascotaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testObtenerTodas() throws Exception {
        Mascota m1 = new Mascota(1L, "Toby", "Perro", 3);
        Mascota m2 = new Mascota(2L, "Michi", "Gato", 1);

        Mockito.when(mascotaService.listarMascotas()).thenReturn(Arrays.asList(m1, m2));
        
        //ejecuta un get de la direccion
        mockMvc.perform(get("/api/mascotas")) //en comparacion con el service no trae datos de tipo codigo sino de json
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$[0].nombre").value("Toby")) //quiero verificar si el nombre de la primera mascota(0) sea toby
                .andExpect(jsonPath("$[1].tipo").value("Gato"));//quiero verificar si el tipo de la segunda mascota(1) sea gato
    }

    @Test
    void testCrearMascota() throws Exception {
        Mascota nueva = new Mascota(null, "Toby", "Perro", 3);
        Mascota guardada = new Mascota(1L, "Toby", "Perro", 3);

        Mockito.when(mascotaService.guardarMascota(any(Mascota.class)))
                .thenReturn(guardada);

        mockMvc.perform(post("/api/v1/mascotas") //lo que pasa aqui es lo que hago a mano en el json
                .contentType(MediaType.APPLICATION_JSON) //le pasare un json
                .content(objectMapper.writeValueAsString(nueva)))//le paso escrito la nueva mascota. va a reemplazar any(Mascota.class) por guardada
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Toby"));
    }

    /*@Test //me debe retornonar que no lo encontre o notFound, debo tenerlo aparte de el encontrarporid porque en el controlador tengo dos respuesta, lo tengo o no lo tengo
    void testObtenerPorIdExistente() throws Exception {
        Mockito.when(mascotaService.obtenerMascotaPorId(99L)).thenReturn(Optional.);

        mockMvc.perform(get("/api/v1/mascotas/99"))
                .andExpect(status().isOk());
    } */

    @Test //me debe retornonar que no lo encontre o notFound, debo tenerlo aparte de el encontrarporid porque en el controlador tengo dos respuesta, lo tengo o no lo tengo
    void testObtenerPorIdNoExistente() throws Exception {
        Mockito.when(mascotaService.obtenerMascotaPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/mascotas/99"))
                .andExpect(status().isNotFound());
    }
}