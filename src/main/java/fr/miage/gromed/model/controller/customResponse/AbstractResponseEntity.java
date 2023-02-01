package fr.miage.gromed.model.controller.customResponse;


public interface AbstractResponseEntity<E,String> {



    public E getCustomBody();

    public void setBody(E body);

}
