package com.service.hydrometrics.controller;

import com.service.hydrometrics.utils.UtilsMethods;
import com.service.hydrometrics.models.DTO.alert.ConfirmAlertDTO;
import com.service.hydrometrics.models.DTO.alert.CorrelationRequestDTO;
import com.service.hydrometrics.models.DTO.alert.CorrelationResponseDTO;
import com.service.hydrometrics.services.IAlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/alert")
@RestController
@RequiredArgsConstructor
public class AlertControll {

    private final IAlertService service;

    @Transactional(readOnly = true)
    @GetMapping("/uncheked")
    public ResponseEntity<?> getAlerts() {
        var alerts = service.getAlertsToReview();
        return ResponseEntity.ok().body(alerts);
    }

    @Transactional
    @PutMapping("/confirm")
    public ResponseEntity<?> confirmAlert(@RequestBody @Valid ConfirmAlertDTO confirm, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(UtilsMethods.validatorBody(result));
        }

        var response = service.processConfirmAlert(confirm);
        if (!response) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/correlation")
    public ResponseEntity<?> getCorrelation(@RequestBody @Valid CorrelationRequestDTO correlationRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(UtilsMethods.validatorBody(result));
        }
        List<CorrelationResponseDTO> response = service.getCorrelationAlert(correlationRequest);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(response);
    }
}
