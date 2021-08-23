package br.com.dev4u.faroldocs.repository;

import br.com.dev4u.faroldocs.domain.DocumentoEtiqueta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentoEtiqueta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentoEtiquetaRepository extends JpaRepository<DocumentoEtiqueta, Long> {}
