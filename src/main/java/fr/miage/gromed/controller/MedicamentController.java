//package fr.miage.gromed.controller;
//
//import fr.miage.gromed.dto.MedicamentDto;
//import fr.miage.gromed.service.metier.MedicamentService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RequestMapping("/api/medicament")
//@RestController
////@Slf4j
//public class MedicamentController {
//    private final MedicamentService medicamentService;
//
//    @Autowired
//    public MedicamentController(MedicamentService medicamentService) {
//        this.medicamentService = medicamentService;
//    }
////
////    @PostMapping
////    public ResponseEntity<Void> save(@RequestBody @Validated MedicamentDto medicamentDto) {
////        medicamentService.save(medicamentDto);
////        return ResponseEntity.ok().build();
////    }
//
////    @GetMapping("/{id}")
////    public ResponseEntity<MedicamentDto> findById(@PathVariable("id") Long id) {
////        MedicamentDto medicament = medicamentService.findById(id);
////        return ResponseEntity.ok(medicament);
////    }
////
////    @DeleteMapping("/{id}")
////    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
////        Optional.ofNullable(medicamentService.findById(id)).orElseThrow(() -> {
////            log.error("Unable to delete non-existent data！");
////            return new ResourceNotFoundException();
////        });
////        medicamentService.deleteById(id);
////        return ResponseEntity.ok().build();
////    }
////
////    @GetMapping("/page-query")
////    public ResponseEntity<Page<MedicamentDto>> pageQuery(MedicamentDto medicamentDto, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
////        Page<MedicamentDto> medicamentPage = medicamentService.findByCondition(medicamentDto, pageable);
////        return ResponseEntity.ok(medicamentPage);
////    }
////
////    @PutMapping("/{id}")
////    public ResponseEntity<Void> update(@RequestBody @Validated MedicamentDto medicamentDto, @PathVariable("id") Long id) {
////        medicamentService.update(medicamentDto, id);
////        return ResponseEntity.ok().build();
////    }
//
//    @GetMapping("/page-query")
//    public ResponseEntity<Page<MedicamentDto>> pageQuery(MedicamentDto medicamentDto, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<MedicamentDto> medicamentPage = medicamentService.findByCondition(medicamentDto, pageable);
//        return ResponseEntity.ok(medicamentPage);
//    }
//
//}