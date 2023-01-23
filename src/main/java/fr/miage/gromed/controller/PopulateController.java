package fr.miage.gromed.controller;

import fr.miage.gromed.service.PopulateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PopulateController {

    @Autowired
    private PopulateService populateService;
    @GetMapping("/populate")
     public String populate() {
        populateService.populateMedicament();
        return "Data has been populated";
    }

}
