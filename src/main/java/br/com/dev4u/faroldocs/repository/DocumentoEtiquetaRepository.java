package br.com.dev4u.faroldocs.repository;

import br.com.dev4u.faroldocs.domain.DocumentoEtiqueta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentoEtiqueta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentoEtiquetaRepository extends JpaRepository<DocumentoEtiqueta, Long> {


@Query("SELECT e FROM DocumentoEtiqueta e WHERE e.documento.id = :id")
Page<DocumentoEtiqueta> findAllEtiquetasDocumento(@Param("id") Long id,Pageable pageable);

}

