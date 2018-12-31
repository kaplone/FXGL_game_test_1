package models;

import com.almasb.fxgl.entity.Entity;

import java.util.Map;

public class Niveau {

    private Map<String, Double> cibles;
    private String nom;

    public Map<String, Double> getCibles() {
        return cibles;
    }

    public void setCibles(Map<String, Double> cibles) {

        this.cibles = cibles;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
