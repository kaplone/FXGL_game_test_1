package app;

public enum EntityType {
    CLE ("CLE"),
    HACHE ("HACHE"),
    CACTUS ("CACTUS"),
    POMME ("POMME"),
    MUR ("MUR"),
    BOUE ("BOUE"),
    JOUEUR ("JOUEUR");

    private String valeur;

    EntityType(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
}
