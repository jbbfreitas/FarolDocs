package br.com.dev4u.faroldocs.repository;

import br.com.dev4u.faroldocs.domain.DocumentoUser;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentoUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentoUserRepository extends JpaRepository<DocumentoUser, Long> {
    @Query("select documentoUser from DocumentoUser documentoUser where documentoUser.user.login = ?#{principal.username}")
    List<DocumentoUser> findByUserIsCurrentUser();
}
