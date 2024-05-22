package com.service.hydrometrics.services.Impl;

import com.service.hydrometrics.client.DataProcessor;
import com.service.hydrometrics.models.DB.entity.WeatherData;
import com.service.hydrometrics.models.DTO.weatherData.WeatherDataReportDTO;
import com.service.hydrometrics.models.enums.ActionLog;
import com.service.hydrometrics.repository.WeatherDataRepository;
import com.service.hydrometrics.services.IWeatherService;
import com.service.hydrometrics.utils.UtilsMethods;
import com.service.hydrometrics.utils.WeatherDataMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService implements IWeatherService {

    private final EntityManager entityManager;
    private final WeatherDataRepository repo;
    private final DataProcessor dataProcessor;

    @Transactional(readOnly = true)
    @Override
    public Optional<WeatherData> getWeatherData(long id) {
        return repo.findById(id);
    }

    public void save(WeatherData weatherData) {
        repo.save(weatherData);
    }

    @Override
    public void update(WeatherData weatherData) {
        repo.save(weatherData);
        UtilsMethods.generatePersistentLogger("WeatherData", ActionLog.UPDATE);
    }

    @Transactional
    @Override
    public Optional<List<WeatherData>> getLastWeekStation(long stationId) {
        List<WeatherData> ListWeatherData = repo.findLastByStationId(168L, stationId);
        if (ListWeatherData.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ListWeatherData);
    }

    @Override
    public Double averageVariableRange(String predictionVariable, String correlationVariable, Double predictionValue) {
        double upRange = predictionValue + ((predictionValue * 0.3) / 100);
        double downRange = predictionValue - ((predictionValue * 0.3) / 100);
        String queryStr = " SELECT AVG(w." + correlationVariable + ") AS average FROM weather_data w " + " LEFT JOIN alert a ON a.weather_data_id = w.id " + " WHERE w." + predictionVariable + " BETWEEN " + downRange + " AND " + upRange + " AND a.weather_data_id IS NULL";
        Query query = entityManager.createNativeQuery(queryStr);
        Object result = query.getSingleResult();
        return result != null ? ((Number) result).doubleValue() : 0.0;
    }

    @Override
    public List<WeatherDataReportDTO> getWeatherDataReport(String startDate, String endDate, long stationId) {
        String queryStr = "SELECT precipitation,  relative_humidity,  solar_radiation,  temperature,  wind_direction,  wind_speed,  date_time,  s.name AS station_name,  (SELECT ar.timestamp  FROM weather_data_aud awd  INNER JOIN audit_revision ar ON ar.id = awd.rev  WHERE awd.date_time = wd.date_time  AND awd.station_id = wd.station_id  AND awd.revtype = 0  ORDER BY ar.timestamp ASC  LIMIT 1) AS creation_date,  COALESCE((SELECT ar.timestamp  FROM weather_data_aud awd INNER JOIN audit_revision ar ON ar.id = awd.rev  WHERE awd.date_time = wd.date_time AND awd.station_id = wd.station_id AND awd.revtype = 1  ORDER BY ar.timestamp ASC  LIMIT 1), 'No Modification Date') AS modification_date,  COALESCE((SELECT u.username  FROM weather_data_aud awd INNER JOIN audit_revision ar ON ar.id = awd.rev INNER JOIN user u ON u.id = ar.user_id  WHERE awd.date_time = wd.date_time AND awd.station_id = wd.station_id AND awd.revtype = 1  ORDER BY ar.timestamp ASC  LIMIT 1), 'No Modification') AS modification_by_user    FROM weather_data wd INNER JOIN station s ON wd.station_id = s.id LIMIT 1";
        Query query = entityManager.createNativeQuery(queryStr);
        List<Object[]> results = query.getResultList();

        List<WeatherDataReportDTO> dtoList = new ArrayList<>();
        for (Object[] result : results) {
            Double precipitation = (Double) result[0];
            Double relativeHumidity = (Double) result[1];
            Double solarRadiation = (Double) result[2];
            Double temperature = (Double) result[3];
            Double windDirection = (Double) result[4];
            Double windSpeed = (Double) result[5];
            Timestamp dateTime = (Timestamp) result[6];
            String stationName = (String) result[7];
            String creationDate = String.valueOf(result[8]);
            String modificationDate = String.valueOf(result[9]);
            String modificationByUser = (String) result[10];
            WeatherDataReportDTO dto = new WeatherDataReportDTO(precipitation, relativeHumidity, solarRadiation, temperature, windDirection, windSpeed, dateTime, stationName, creationDate, modificationDate, modificationByUser);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public void saveJsonWeatherDatas(List<WeatherDataMapper> weatherDataMappers) {
        dataProcessor.sendWeatherDataMappers(weatherDataMappers);
    }
}
