package com.service.hydrometrics.repository;

import com.service.hydrometrics.models.DB.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    @Query(value = "SELECT wd.* FROM weather_data wd WHERE wd.station_id = :stationId ORDER BY wd.date_time DESC LIMIT :N", nativeQuery = true)
    List<WeatherData> findLastByStationId(@Param("N") long N, @Param("stationId") long stationId);

    @Query(value = "SELECT AVG(w.:variable) AS average FROM weather_data w LEFT JOIN alert a ON a.weather_data_id = w.id " +
            "WHERE :variable BETWEEN :upRange AND :downRange AND a.weather_data_id IS NULL", nativeQuery = true)
    Double averageVariable(@Param("variable")String variable, @Param("upRange")Double upRange, @Param("downRange")Double downRange);

    Optional<WeatherData> findByStationIdAndDateTime(long stationId, Timestamp dateTime);

}