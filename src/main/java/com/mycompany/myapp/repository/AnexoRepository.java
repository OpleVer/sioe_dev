package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Anexo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Anexo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnexoRepository extends JpaRepository<Anexo,Long> {
    
}
