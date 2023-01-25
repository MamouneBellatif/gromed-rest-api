package fr.miage.gromed.controller;
import org.springframework.beans.factory.annotation.Autowired;

public class PanierController {
   @Authowuired
    private PanierService panierService; 


    @GetMapping
    public List<Medicament> getAllMedicaments() {
        return panierService.getAllMedicaments();
    }

    @PostMapping
    public boolean addMedicament(@RequestBody Medicament medicament) {
        return panierService.addMedicament(medicament);
    }

    @PutMapping
    public boolean updateMedicament(@RequestBody Medicament medicament) {
        return panierService.updateMedicament(medicament);
    }

    @DeleteMapping("/{cip}")
    public boolean deleteMedicament(@PathVariable String cip) {
        return panierService.deleteMedicament(cip);
    }
}
