package models;

import app.EntityType;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.almasb.fxgl.entity.Entity;

public class Labyrinthe {

    private Integer sizeX;
    private Integer sizeY;
    private static List<Entity> murs;
    private static Map<Entity, Mur> mursHit;

    public Labyrinthe(Integer sizeX, Integer sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public static List<Entity> getMurs(String codes, GameWorld gameWorld){
        if (murs != null) {
            return murs;
        }
        else {
            return makeMurs(codes, gameWorld);
        }
    }

    public static List<Entity> makeMurs(String codes, GameWorld gameWorld){
        murs = new ArrayList<>();
        mursHit = new HashMap<>();
        String[] lignes = codes.split("\n");
        int x = 10;
        int y = 50;
        int posX = 50;
        int posY = 50;
        int deltaY = 10;
        for (int i = 0; i < lignes.length; i++){
            String[] elements = lignes[i].split("");
            if (i % 2 == 0){
                x = 60;
                y = 10;
                deltaY = 0;
                for (int j = 0; j < elements.length; j++){
                    if ("_".equals(elements[j])){
                        Entity mur = Entities.builder()
                                .type(EntityType.MUR)
                                .at(posX, posY)
                                .viewFromNodeWithBBox(new Rectangle(x, y, Color.rgb(i * j * 23 % 255, i * j * 81 % 255,i * j * 186 % 255)))
                                .with(new CollidableComponent(true), new PhysicsComponent())
                                .buildAndAttach(gameWorld);

                        murs.add(mur);
                        mursHit.put(mur, new Mur(mur));
                    }
                    posX += 50;
                }


            }
            else {
                x = 10;
                y = 50;
                deltaY = 50;
                for (int j = 0; j < elements.length; j++){
                    if ("|".equals(elements[j])){
                        Entity mur = Entities.builder()
                                .type(EntityType.MUR)
                                .at(posX, posY)
                                .viewFromNodeWithBBox(new Rectangle(x, y, Color.rgb(i * j * 223 % 255, i * j * 131 % 255,i * j * 16 % 255)))
                                .with(new CollidableComponent(true), new PhysicsComponent())
                                .buildAndAttach(gameWorld);
                        murs.add(mur);
                        mursHit.put(mur, new Mur(mur));
                    }
                    posX += 50;
                }
            }
            posY += deltaY;
            posX = 50;

        }
        return murs;
    }

    public static List<Entity> getMurs() {
        return murs;
    }

    public static Map<Entity, Mur> getMursHit() {
        return mursHit;
    }

    public Integer getSizeX() {
        return sizeX;
    }

    public void setSizeX(Integer sizeX) {
        this.sizeX = sizeX;
    }

    public Integer getSizeY() {
        return sizeY;
    }

    public void setSizeY(Integer sizeY) {
        this.sizeY = sizeY;
    }
}
