package ControlCenter.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ControlCenter.dto.OrgUnitResponseDTO;
import ControlCenter.service.OrgUnitService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cc/orgUnit")
public class OrgUnitController {

    OrgUnitService orgUnitService;

    @GetMapping("/orgUnits")
    public ResponseEntity<List<OrgUnitResponseDTO>> getOrgUnits(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate,
            @RequestParam String status) {
        List<OrgUnitResponseDTO> result = orgUnitService.getOrgUnits(asOfDate, status.toLowerCase());
        return ResponseEntity.ok(result);
    }

}
