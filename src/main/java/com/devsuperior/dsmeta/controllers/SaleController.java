package com.devsuperior.dsmeta.controllers;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.services.SaleService;

import java.util.List;

@RestController
@RequestMapping(value = "/sales")
public class SaleController {

    @Autowired
    private SaleService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<SaleMinDTO> findById(@PathVariable Long id) {
        SaleMinDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/report")
    public ResponseEntity<?> getReport() {
        // TODO
        return null;
    }

    @GetMapping(value = "/summary")
    public ResponseEntity<List<SaleSummaryDTO>> searchSummary(@RequestParam(name = "minDate", defaultValue = "") String minDate,
                                                              @RequestParam(name = "maxDate", defaultValue = "") String maxDate,
                                                              @RequestParam(name = "name", defaultValue = "") String name) {
        List<SaleSummaryDTO> list = service.searchSummary(minDate, maxDate, name);
        return ResponseEntity.ok(list);
    }
}
