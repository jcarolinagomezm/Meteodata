package com.service.hydrometrics.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.hydrometrics.models.DB.entity.Station;
import com.service.hydrometrics.models.DB.entity.User;
import com.service.hydrometrics.models.enums.Role;
import com.service.hydrometrics.repository.StationRepository;
import com.service.hydrometrics.services.Impl.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    @Value("${init.data.enabled}")
    private boolean initDataEnabled;
    @Value("${init.stations.enabled}")
    private boolean initStationsEnabled;
    @Value("${init.users.enabled}")
    private boolean initUsersEnabled;

    private final WeatherService weatherService;
    private final StationRepository stationRepo;
    private final UserService userService;
    private final EntityManager entityManager;
    String[] stationNames = {"Apto_Guaymaral", "Bolivia", "Bosa", "Carvajal_Sevillana", "Centro_de_Alto_Rendimiento", "Ciudad_Bolivar", "Colina", "Doña_Juana", "Fontibon", "Guaymaral", "Jazmin", "Kennedy", "Las_Ferias", "Min_Ambiente", "Movil_7ma", "Movil_Fontibon", "Puente_Aranda", "Ramada", "San_Cristobal", "Suba", "Tunal", "Usaquen", "Usme"};


    public void initUsers() {
//        System.out.println("Creating Users...");
        User admin = new User("admin", "admin", "admin", "admin", "admin@hydrometrics.com", Role.ADMIN, true);
        User auditor = new User("auditor", "auditor", "auditor", "auditor", "auditor@hydrometrics.com", Role.AUDITOR, true);
        User user = new User("user", "user", "user", "user", "user@hydrometrics.com", Role.USER, true);
        userService.saveUser(admin);
        userService.saveUser(auditor);
        userService.saveUser(user);
//        System.out.println("Users created !!!");

    }

    public void initStations() {
//        System.out.println("Creating Stations...");
        for (String stationName : stationNames) {
//            System.out.println("...");
            Station station = new Station(stationName);
            stationRepo.save(station);
        }
//        System.out.println("Stations created !!!");
    }

    @PostConstruct
    public void init() {
        if (initUsersEnabled) {
            initUsers();
        }
        if (initStationsEnabled) {
            initStations();
        }
        if (initDataEnabled) {
            System.out.println("Initializing data of WeatherData...");
            ObjectMapper objectMapper = new ObjectMapper();
            try (InputStream inputStream = new ClassPathResource("/JSON/weather_data.json").getInputStream()) {
                List<WeatherDataMapper> weatherDataMappers = objectMapper.readValue(inputStream, new TypeReference<List<WeatherDataMapper>>() {
                });
                List<WeatherData> weatherDatas = weatherService.WeatherDataMapperToEntity(weatherDataMappers);
                if (!weatherDatas.isEmpty()) {
                    weatherService.saveAll(weatherDatas);
                }
                System.out.println("End data Initialization of WeatherData...");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

//            LocalDateTime dateTime = LocalDateTime.of(2020, 1, 1, 0, 0);
//            Random random = new Random();
//            List<Station> stations = stationRepo.getAll();
//            for (Station station : stations) {
//                for (int i = 0; i < 100; i++) { // Generar datos para 100 horas
//                    WeatherData weatherData = new WeatherData();
//                    weatherData.setStation(station);
//                    weatherData.setDateTime(Timestamp.valueOf(dateTime));
//                    weatherData.setTemperature(random.nextFloat() * 26); // Temperatura entre 0 y 26
//                    weatherData.setWindDirection(random.nextFloat() * 360); // Dirección del viento entre 0 y 360
//                    weatherData.setRelativeHumidity(random.nextFloat() * 100); // Humedad relativa entre 0 y 100
//                    weatherData.setSolarRadiation(random.nextFloat() * 1400); // Radiación solar entre 0 y 1400
//                    weatherData.setPrecipitation(random.nextFloat() * 60); // Precipitación entre 0 y 60
//                    weatherData.setWindSpeed(random.nextFloat() * 10); // Velocidad del viento entre 0 y 10
//                    // Simular errores en el 30% de los datos
//                    if (random.nextDouble() < 0.2) {
//                        // Simular error cambiando algunos valores
//                        weatherData.setTemperature(random.nextFloat() * 50); // Temperatura con valores erróneos
//                        weatherData.setWindDirection(random.nextFloat() * 720); // Dirección del viento con valores erróneos
//                        weatherData.setRelativeHumidity(random.nextFloat() * 150); // Humedad relativa con valores erróneos
//                        weatherData.setSolarRadiation(random.nextFloat() * 2000); // Radiación solar con valores erróneos
//                        weatherData.setPrecipitation(random.nextFloat() * 100); // Precipitación con valores erróneos
//                        weatherData.setWindSpeed(random.nextFloat() * 20); // Velocidad del viento con valores erróneos
//                    }
//                    weatherService.save(weatherData);
//                    // Avanzar al siguiente hora
//                    dateTime = dateTime.plusHours(1);
        }
    }
}