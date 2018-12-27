package models;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.HitBox;

public class Mur {

    private Entity shape;
    private HitBox hitBox;

    public Mur(Entity shape) {
        this.shape = shape;
        this.hitBox = shape.getBoundingBoxComponent().hitBoxesProperty().get(0);
    }

    public Entity getShape() {
        return shape;
    }

    public void setShape(Entity shape) {
        this.shape = shape;
    }

    public HitBox getHitBox() {
        return hitBox;
    }

    public void setHitBox(HitBox hitBox) {
        this.hitBox = hitBox;
    }
}
