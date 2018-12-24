package app;

import com.almasb.fxgl.physics.CollisionHandler;

public class JoueurCollisionHandler extends CollisionHandler {

    private double xPrecedent;
    private double yPrecedent;


    /**
     * The order of types determines the order of entities in callbacks.
     *
     * @param a entity type of the first entity
     * @param b entity type of the second entity
     */
    public JoueurCollisionHandler(Object a, Object b) {
        super(a, b);
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
