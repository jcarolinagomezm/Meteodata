package com.service.hydrometrics.repository;

import com.service.hydrometrics.models.DB.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    @Query("select s from Station s")
    List<Station> getAll();
}