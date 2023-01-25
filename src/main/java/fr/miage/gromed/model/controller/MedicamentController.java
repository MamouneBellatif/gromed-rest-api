package fr.miage.gromed.model.controller;

import com.sun.tools.javac.util.DefinedBy.Api;
import fr.miage.gromed.model.dto.MedicamentDto;
import fr.miage.gromed.model.mapper.MedicamentMapper;
import fr.miage.gromed.model.medicament.Medicament;
import fr.miage.gromed.model.service.MedicamentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/medicament")
@RestController
@Slf4j
@Api("medicament")
public class MedicamentController {
    private final MedicamentService medicamentService;

    public MedicamentController(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Validated MedicamentDto medicamentDto) {
        medicamentService.save(medicamentDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentDto> findById(@PathVariable("id") Long id) {
        MedicamentDto medicament = medicamentService.findById(id);
        return ResponseEntity.ok(medicament);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Optional.ofNullable(medicamentService.findById(id)).orElseThrow(() -> {
            log.error("Unable to delete non-existent data！");
            return new ResourceNotFoundException();
        });
        medicamentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page-query")
    public ResponseEntity<Page<MedicamentDto>> pageQuery(MedicamentDto medicamentDto, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MedicamentDto> medicamentPage = medicamentService.findByCondition(medicamentDto, pageable);
        return ResponseEntity.ok(medicamentPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Validated MedicamentDto medicamentDto, @PathVariable("id") Long id) {
        medicamentService.update(medicamentDto, id);
        return ResponseEntity.ok().build();
    }
}