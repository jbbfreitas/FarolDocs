package br.com.dev4u.faroldocs.repository;

import br.com.dev4u.faroldocs.domain.OrgaoEmissor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrgaoEmissor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgaoEmissorRepository extends JpaRepository<OrgaoEmissor, Long> {}
