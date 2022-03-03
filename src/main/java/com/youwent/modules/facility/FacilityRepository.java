package com.youwent.modules.facility;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    Facility findByBuilding(String building);
}
