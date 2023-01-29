package fr.miage.gromed.controller;

import fr.miage.gromed.service.metier.PopulateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PopulateController {

    @Autowired
    private PopulateService populateService;
    @GetMapping("/populateMedicament")
     public String populate() {
//        populateService.populateMedicament();
        return "medicaments has been populated";
    }

    @GetMapping("/getCsv")
     public String populatePresentation() {
        populateService.parseCisLibelle();
        return "fichier generé";
    }

    @GetMapping("/populate")
    public String populateAll(){
        populateService.populateMedicament();
        populateService.populatePresCached();
        populateService.initStock();
        return "pres populate";
    }

    @GetMapping("populUrls")
    public String populateUrls(){
        populateService.populateUrls();
        return "urls populate";
    }
    @GetMapping("/populateCompCache")
    public String populateSubsCache(){
//        populateService.populateComposantCached();
        return "comp populate cached";
    }

    @GetMapping("/populateCond")
    public String populateCond(){
//        populateService.populateConditions();
        return "cond populate";
    }

    @GetMapping("/populateComp")
    public String populateSubs(){
//        populateService.populateComposant();
        return "comp populate";
    }

    @GetMapping("/populateInfo")
    public String populateInfo(){
//        populateService.populateInfos();
        return "info populate";
    }

    @GetMapping("/populateAvis")
    public String populateAvis(){
//        populateService.populateAvis();
        return "avis populate";
    }

    

}
