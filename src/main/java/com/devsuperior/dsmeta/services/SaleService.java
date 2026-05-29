package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

    @Autowired
    private SaleRepository repository;

    public SaleMinDTO findById(Long id) {
        Optional<Sale> result = Optional.of(repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(("Resource not found."))
        ));
        Sale entity = result.get();
        return new SaleMinDTO(entity);
    }

    public List<SaleSummaryDTO> searchSummary(String minDate, String maxDate) {
        LocalDate max;
        if (maxDate == null || maxDate.isBlank()) {
            max = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        } else {
            max = LocalDate.parse(maxDate);
        }

        LocalDate min;
        if (minDate == null || minDate.isBlank()) {
            min = max.minusYears(1L);
        } else {
            min = LocalDate.parse(minDate);
        }

        return repository.searchSummary(min, max);
    }

    public Page<SaleMinDTO> getReport(String minDate, String maxDate, String name, Pageable pageable) {
        LocalDate max;
        if (maxDate == null || maxDate.isBlank()) {
            max = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        } else {
            max = LocalDate.parse(maxDate);
        }

        LocalDate min;
        if (minDate == null || minDate.isBlank()) {
            min = max.minusYears(1L);
        } else {
            min = LocalDate.parse(minDate);
        }

        return repository.getReport(min, max, name, pageable);
    }
}
