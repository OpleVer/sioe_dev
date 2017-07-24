package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Peticion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Peticion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeticionRepository extends JpaRepository<Peticion,Long> {
    
}
