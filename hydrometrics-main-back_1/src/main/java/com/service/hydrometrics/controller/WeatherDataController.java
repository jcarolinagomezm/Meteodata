package com.service.hydrometrics.controller;

import com.service.hydrometrics.utils.WeatherDataMapper;
import com.service.hydrometrics.models.DB.entity.WeatherData;
import com.service.hydrometrics.models.DTO.graphics.DataDTO;
import com.service.hydrometrics.models.DTO.graphics.Variable;
import com.service.hydrometrics.models.enums.DataCamp;
import com.service.hydrometrics.services.Impl.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
public class WeatherDataController {

    private final WeatherService service;

    @GetMapping("/{stationId}")
    public ResponseEntity<?> getLastWeekStation(@PathVariable long stationId) {
        Optional<List<WeatherData>> oWeathertDatas = service.getLastWeekStation(stationId);
        if (oWeathertDatas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<Variable> dataSet = new ArrayList<>();
        for (DataCamp dataCamp : DataCamp.values()) {
            Variable variable = new Variable();
            variable.setDataCamp(dataCamp.toString());
            for (WeatherData wd : oWeathertDatas.get()) {
                DataDTO data = new DataDTO(wd.getDateTime().toString(), wd.getValueVariable(dataCamp));
                variable.setData(data);
            }
            dataSet.add(variable);
        }

        return ResponseEntity.ok(dataSet);
    }
//
//    @PostMapping("/saveDatas")
//    public ResponseEntity<?> saveAllFromJson(@RequestBody List<WeatherDataMapper> weatherDataMappers) {
//        service.saveJsonWeatherDatas(weatherDataMappers);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/report")
    public ResponseEntity<?> weatherDataReport(@RequestParam String startDate, @RequestParam String endDate, @RequestParam Long stationId) {
        return ResponseEntity.ok(service.getWeatherDataReport(startDate, endDate, stationId));
    }
}
