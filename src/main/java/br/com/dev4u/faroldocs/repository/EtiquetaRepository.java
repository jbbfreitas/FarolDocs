package br.com.dev4u.faroldocs.repository;

import br.com.dev4u.faroldocs.domain.Etiqueta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Etiqueta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {}
