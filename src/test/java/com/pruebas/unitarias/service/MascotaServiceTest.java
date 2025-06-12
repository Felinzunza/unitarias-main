package com.pruebas.unitarias.service;

import com.pruebas.unitarias.model.Mascota;
import com.pruebas.unitarias.repository.MascotaRepository; 
import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
//"replica"de la capa de servicios

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class MascotaServiceTest {
    //se configura un entorno virtual para prueba
     //esto no corre en la carpeta normal, corre en el test
     //con mock voy a hacer una maqueta de la base de datos, es un simulador de lo que deberia pasar.
    @Mock
    private MascotaRepository mascotaRepository;

    //injectmock es la capa que quiero probar
    @InjectMocks
    private MascotaService mascotaService;

    //se inicializa, es como un constructor
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /* Test para guardar mascota en la capa servicio */
    @Test
    void testGuardarMascota() {
    // 🔧 Se crea una mascota de prueba que será pasada al método del servicio
    // El ID es null porque se supone que será generado automáticamente al guardar
    Mascota mascota = new Mascota(null, "Rex", "Perro", 5);

    // 📥 Se define la mascota que el mock debe devolver como si hubiera sido guardada en base de datos
    Mascota mascotaGuardada = new Mascota(1L, "Rex", "Perro", 5);

    // 🧪 Se configura el mock del repositorio: cuando se llame a save con 'mascota',
    // debe retornar 'mascotaGuardada'. Esto simula el comportamiento del repositorio.
    when(mascotaRepository.save(mascota)).thenReturn(mascotaGuardada);
        

        //excepcion es que el id sea igual a 1
        Mascota resultado = mascotaService.guardarMascota(mascota);
        //  Evalúa que el ID del resultado sea 1
        assertThat(resultado.getId()).isEqualTo(1L);

        //  Evalúa que el nombre sea exactamente "Rex"
        assertThat(resultado.getNombre()).isEqualTo("Rex");

        //  Evalúa que el tipo NO sea "Gato" (se espera "Perro")
        assertThat(resultado.getTipo()).isNotEqualTo("Gato");

        //  Evalúa que la edad sea 5
        assertThat(resultado.getEdad()).isEqualTo(5);

        //  Evalúa que el método save() fue efectivamente llamado una vez con el objeto mascota
        verify(mascotaRepository).save(mascota);

        //ejecutar prueba desde service test 
         //ahi se mostrara si es que hay un error en cada assertthat
    }

    @Test
    void testListarMascotas() {
        Mascota m1 = new Mascota(1L, "Rex", "Perro", 5);
        Mascota m2 = new Mascota(2L, "Michi", "Gato", 2);
        //Me debe retornar estas mascotas cuando obtenga la lista en la base de datos
        when(mascotaRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        //
        List<Mascota> resultado = mascotaService.listarMascotas();
        assertThat(resultado).hasSize(2).contains(m1, m2); //voy a comprobar que el tamaño de la lista sea 2 y que contengan esas dos mascotas
        verify(mascotaRepository).findAll();
    }

    @Test
    void testBuscarMascotaXId(Long id){
        Mascota mas1 = new Mascota(1L, "Rex", "Perro", 5);
  
        when(mascotaRepository.findById(id)).thenReturn(Optional.of(mas1));
        Optional<Mascota> resultado = mascotaService.obtenerMascotaPorId(id);

       
    }


}