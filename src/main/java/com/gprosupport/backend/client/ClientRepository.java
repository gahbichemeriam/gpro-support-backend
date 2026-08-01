package com.gprosupport.backend.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c JOIN FETCH c.projetErp JOIN FETCH c.versionActive " +
           "WHERE c.projetErp.id = :projetId")
    List<Client> findByProjetErpId(@Param("projetId") Long projetId);

    @Query("SELECT c FROM Client c JOIN FETCH c.projetErp JOIN FETCH c.versionActive")
    List<Client> findAllWithRelations();

    @Query("SELECT c FROM Client c JOIN FETCH c.projetErp JOIN FETCH c.versionActive " +
           "WHERE c.versionActive.id = :versionId")
    List<Client> findClientsParVersionActive(@Param("projetId") Long projetId,
                                             @Param("versionId") Long versionId);
}
