package br.com.dev4u.faroldocs.repository;

import br.com.dev4u.faroldocs.domain.TipoNorma;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoNorma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoNormaRepository extends JpaRepository<TipoNorma, Long> {}
