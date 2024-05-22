package com.service.hydrometrics.client;

import com.service.hydrometrics.models.DB.entity.WeatherData;
import com.service.hydrometrics.utils.WeatherDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Service
public class DataProcessor {

    private final WebClient.Builder webClientBuilder;

    public void sendWeatherDataMappers(List<WeatherDataMapper> weatherDataMappers) {
        String url = "http://localhost:8081/process"; // Ajusta esta URL según tu configuración
        WebClient webClient = webClientBuilder.build();
        webClient.post()
                .uri(url)
                .body(Mono.just(weatherDataMappers), new ParameterizedTypeReference<List<WeatherData>>() {})
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(log::info);
    }
}
