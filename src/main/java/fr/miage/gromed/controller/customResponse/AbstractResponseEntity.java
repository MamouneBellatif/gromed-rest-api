package fr.miage.gromed.controller.customResponse;


public interface AbstractResponseEntity<E,String> {



    public E getCustomBody();

    public void setBody(E body);

}
