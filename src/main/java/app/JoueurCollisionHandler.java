package app;

import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.HitBox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JoueurCollisionHandler extends CollisionHandler {

    private double xPrecedent;
    private double yPrecedent;
    private static Map<Entity, HitBox> murs;
    private static Set<Entity> mursToRemove = new HashSet<>();
    private static HitBox hitBoxJoueur;


    /**
     * The order of types determines the order of entities in callbacks.
     *
     * @param a entity type of the first entity
     * @param b entity type of the second entity
     */
    public JoueurCollisionHandler(Object a, Object b) {
        super(a, b);
        murs = new HashMap<>();
    }

    public static void clear(){
        murs.clear();;
    }

    public static Map<Entity, HitBox> getMurs() {
        return murs;
    }

    public static void addMurs(Entity mur, HitBox h) {
        murs.put(mur, h);
    }

    public static void addMurToRemove(Entity mur) {
        mursToRemove.add(mur);
    }

    public static HitBox getHitBoxJoueur() {
        return hitBoxJoueur;
    }

    public static void setHitBoxJoueur(HitBox hitBoxJoueur) {
        JoueurCollisionHandler.hitBoxJoueur = hitBoxJoueur;
    }

    public static Set<Entity> getMursToRemove() {
        return mursToRemove;
    }

    public static void removeMur(Entity mur) {
        murs.remove(mur);
    }

    public static void clearMursToRemove(){
        mursToRemove.clear();
    }

    public static void removeAllMurs(Set<Entity> m) {
        for (Entity m_ : m){
            murs.remove(m_);
        }

    }

    public static void setMurs(Map<Entity, HitBox> murs) {
        JoueurCollisionHandler.murs = murs;
    }


    public double getxPrecedent() {
        return xPrecedent;
    }

    public void setxPrecedent(double xPrecedent) {
        this.xPrecedent = xPrecedent;
    }

    public double getyPrecedent() {
        return yPrecedent;
    }

    public void setyPrecedent(double yPrecedent) {
        this.yPrecedent = yPrecedent;
    }
}
