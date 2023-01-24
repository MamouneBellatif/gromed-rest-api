package fr.miage.gromed.controller;

import fr.miage.gromed.service.PopulateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PopulateController {

    @Autowired
    private PopulateService populateService;
    @GetMapping("/populateMedicament")
     public String populate() {
        populateService.populateMedicament();
        return "medicaments has been populated";
    }

//    @GetMapping("/populatePres")
//    public String populatePres(){
//        populateService.populatePresentation();
//        return "pres populate";
//    }
    @GetMapping("/populatePresCached")
    public String populatePresCached(){
        populateService.populatePresCached();
        return "pres populate cached";
    }

    @GetMapping("/populateCompCache")
    public String populateSubsCache(){
        populateService.populateComposantCached();
        return "comp populate cached";
    }

    @GetMapping("/populateComp")
    public String populateSubs(){
        populateService.populateComposant();
        return "comp populate";
    }

    @GetMapping("/populateAvis")
    public String populateAvis(){
        populateService.populateAvis();
        return "avis populate";
    }

}
