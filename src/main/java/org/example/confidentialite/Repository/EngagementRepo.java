package org.example.confidentialite.Repository;

import org.example.confidentialite.Entity.Engagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EngagementRepo extends JpaRepository<Engagement,Long> {

    @Query("select e.statut from Engagement e where e.personnel.IdPersonnel = :idPersonnel")
    Optional<String> findByIdPersonnel(@Param("idPersonnel") String idPersonnel);
}
