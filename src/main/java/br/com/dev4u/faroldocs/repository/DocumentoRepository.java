package br.com.dev4u.faroldocs.repository;

import br.com.dev4u.faroldocs.domain.Documento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Documento entity.
 */
@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    @Query(
        value = "select distinct documento from Documento documento left join fetch documento.etiquetas left join fetch documento.users",
        countQuery = "select count(distinct documento) from Documento documento"
    )
    Page<Documento> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct documento from Documento documento left join fetch documento.etiquetas left join fetch documento.users")
    List<Documento> findAllWithEagerRelationships();

    @Query(
        "select documento from Documento documento left join fetch documento.etiquetas left join fetch documento.users where documento.id =:id"
    )
    Optional<Documento> findOneWithEagerRelationships(@Param("id") Long id);
}
