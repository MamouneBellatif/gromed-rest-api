package fr.miage.gromed.dto;

import java.io.Serializable;
import java.util.List;

import fr.miage.gromed.model.PanierItem;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Setter
@Getter
public class PanierDto implements Serializable{
    private List<PanierItem> items; 
    

}