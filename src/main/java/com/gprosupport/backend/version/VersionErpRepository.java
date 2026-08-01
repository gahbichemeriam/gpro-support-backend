package com.gprosupport.backend.version;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VersionErpRepository extends JpaRepository<VersionErp, Long> {

    @Query("SELECT v FROM VersionErp v JOIN FETCH v.projetErp WHERE v.projetErp.id = :projetId")
    List<VersionErp> findByProjetErpId(@Param("projetId") Long projetId);

    @Query("SELECT v FROM VersionErp v JOIN FETCH v.projetErp")
    List<VersionErp> findAllWithProjet();

    @Query("SELECT v FROM VersionErp v JOIN FETCH v.projetErp " +
           "WHERE v.projetErp.id = :projetId AND v.codeVersion = :codeVersion")
    Optional<VersionErp> findByProjetErpIdAndCodeVersion(@Param("projetId") Long projetId,
                                                          @Param("codeVersion") String codeVersion);

    @Query("SELECT v FROM VersionErp v JOIN FETCH v.projetErp " +
           "WHERE v.projetErp.id = :projetId AND v.statut = :statut")
    List<VersionErp> findByProjetErpIdAndStatut(@Param("projetId") Long projetId,
                                                 @Param("statut") StatutVersion statut);

    boolean existsByProjetErpIdAndCodeVersion(Long projetId, String codeVersion);
}
