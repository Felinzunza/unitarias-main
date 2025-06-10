package com.pruebas.unitarias.repository;


import com.pruebas.unitarias.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long>  {
    
}
