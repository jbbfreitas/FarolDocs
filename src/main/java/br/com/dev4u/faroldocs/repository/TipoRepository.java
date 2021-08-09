package br.com.dev4u.faroldocs.repository;

import br.com.dev4u.faroldocs.domain.Tipo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tipo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoRepository extends JpaRepository<Tipo, Long> {}
