package fr.miage.gromed.controller;

import fr.miage.gromed.service.PopulateService;
import fr.miage.gromed.utils.DataWrapper;
import fr.miage.gromed.utils.MedicalDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PopulateController {

    @Autowired
    private PopulateService populateService;
    @GetMapping("/populate")
     public String populate() {
        populateService.populate();
        return "Data has been populated";
    }

}
