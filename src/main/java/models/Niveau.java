package models;

import com.almasb.fxgl.entity.Entity;

import java.util.List;
import java.util.Map;

public class Niveau {

    private List<But> cibles;
    private List<Item> items;
    private String nom;
    private List<String> dessin;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<String> getDessin() {
        return dessin;
    }

    public void setDessin(List<String> dessin) {
        this.dessin = dessin;
    }

    public List<But> getCibles() {
        return cibles;
    }

    public void setCibles(List<But> cibles) {

        this.cibles = cibles;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
