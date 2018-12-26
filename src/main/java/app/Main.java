package app;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.settings.GameSettings;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import models.Labyrinthe;

import java.util.*;

import static app.EnumDirection.*;

public class Main extends GameApplication {

    private Entity player;
    private List<Entity> pommes;
    private Entity pomme;
    private GameSettings settings;
    private Integer sizeX = 600;
    private Integer sizeY = 600;
    private Integer sizePlayerX = 25;
    private Integer sizePlayerY = 25;
    private Integer maxLeft;
    private Integer maxRight;
    private Integer maxTop;
    private Integer maxBottom;
    private Integer vitesse;

    @Override
    protected void initSettings(GameSettings settings) {
        this.settings = settings;
        settings.setWidth(sizeX);
        settings.setHeight(sizeY);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");
        settings.setProfilingEnabled(true);
    }

    @Override
    protected void initGame() {

        vitesse = 1;

        player = Entities.builder()
                .type(EntityType.JOUEUR)
                .at(sizeX / 2, sizeY / 2)
                .viewFromNodeWithBBox(new Rectangle(sizePlayerX, sizePlayerY, Color.BLUE))
                .with(new CollidableComponent(true))
                .buildAndAttach(getGameWorld());
        player.setProperty("canMoveRight", true);
        player.setProperty("canMoveLeft", true);
        player.setProperty("canMoveUp", true);
        player.setProperty("canMoveDown", true);
        player.setProperty("Etat", "normal");
        player.setProperty("prevDirection", NONE);
        player.setProperty("nextDirection", NONE);

        pommes = new ArrayList<>();

        pomme = Entities.builder()
                .type(EntityType.POMME)
                .at(sizeX / 4, sizeY / 4)
                .viewFromNodeWithBBox(new Circle(10, Color.LIMEGREEN))
                .with(new CollidableComponent(true), new PhysicsComponent())
                .buildAndAttach(getGameWorld());
        pommes.add(pomme);
        pomme = Entities.builder()
                .type(EntityType.POMME)
                .at(3 * (sizeX / 4), sizeY / 4)
                .viewFromNodeWithBBox(new Circle(10, Color.GREEN))
                .with(new CollidableComponent(true), new PhysicsComponent())
                .buildAndAttach(getGameWorld());
        pommes.add(pomme);
        pomme = Entities.builder()
                .type(EntityType.POMME)
                .at(3 * (sizeX / 4), 3 * (sizeY / 4))
                .viewFromNodeWithBBox(new Circle(10, Color.LIME))
                .with(new CollidableComponent(true), new PhysicsComponent())
                .buildAndAttach(getGameWorld());
        pommes.add(pomme);
        pomme = Entities.builder()
                .type(EntityType.POMME)
                .at(sizeX / 4, 3 * (sizeY / 4))
                .viewFromNodeWithBBox(new Circle(10, Color.ORANGE))
                .with(new CollidableComponent(true), new PhysicsComponent())
                .buildAndAttach(getGameWorld());
        pommes.add(pomme);

        String s =

                "__________" + "\n"
                        + "  |  | |  |" + "\n"
                        + "  __    _ " + "\n"
                        + "||  ||| | |" + "\n"
                        + " __   __  " + "\n"
                        + "||  | |  ||" + "\n"
                        + "_ ____ ___" + "\n"
                        + "| ||  | | |" + "\n"
                        + "  __    _ " + "\n"
                        + "  |  | |  |" + "\n"
                        + "  __    _ " + "\n"
                        + "  |  | |  |" + "\n"
                        + "  __    _ " + "\n";

        Labyrinthe.getMurs(s, getGameWorld());
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        maxRight = sizeX;
        maxLeft = -sizePlayerX;
        maxTop = -sizePlayerY;
        maxBottom = sizeY;

        input.addAction(new UserAction("Clic") {
            @Override
            protected void onAction() {
                player.setPosition(sizeX / 2, sizeY / 2);
                getGameState().setValue("pixelsMovedX", sizeX / 2);
                getGameState().setValue("pixelsMovedY", sizeY / 2);
            }
        }, MouseButton.PRIMARY);

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {

                System.out.println("Action begin");
            }

            @Override
            protected void onAction() {

                if (player.getProperties().getBoolean("canMoveRight")) {
                    // si l'objet sort par la droite il rentre par la gauche
                    if (getGameState().getInt("pixelsMovedX") >= maxRight) {
                        maxLeft = -sizePlayerX;
                        maxRight = sizeX;
                        player.setPosition(maxLeft, player.getY());
                        getGameState().setValue("pixelsMovedX", maxLeft);
                    } else {
                        player.translateX(vitesse); // move right 5 pixels
                        getGameState().increment("pixelsMovedX", vitesse);
                    }
                }
            }
        }, KeyCode.RIGHT);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onActionBegin() {
                System.out.println("Action begin");
            }

            @Override
            protected void onAction() {

                if (player.getProperties().getBoolean("canMoveLeft")) {
                    // si l'objet sort par la gauche il rentre par a droite
                    //System.out.println(getGameState().getInt("pixelsMovedX") + " : " +  maxLeft);
                    if (getGameState().getInt("pixelsMovedX") <= maxLeft) {
                        maxLeft = -sizePlayerX;
                        maxRight = sizeX;
                        player.setPosition(maxRight, player.getY());
                        getGameState().setValue("pixelsMovedX", maxRight);
                    } else {
                        player.translateX(-vitesse); // move right 5 pixels
                        getGameState().increment("pixelsMovedX", -vitesse);
                    }
                }
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onActionBegin() {
                System.out.println("Action begin");
            }

            @Override
            protected void onAction() {

                if (player.getProperties().getBoolean("canMoveUp")) {
                    // si l'objet sort par le haut il rentre par le bas
                    // System.out.println(getGameState().getInt("pixelsMovedY") + " : " +  maxTop);
                    if (getGameState().getInt("pixelsMovedY") <= maxTop) {
                        maxTop = -sizePlayerY;
                        maxBottom = sizeY;
                        player.setPosition(player.getX(), maxBottom);
                        getGameState().setValue("pixelsMovedY", maxBottom);
                    } else {
                        player.translateY(-vitesse); // move right 5 pixels
                        getGameState().increment("pixelsMovedY", -vitesse);
                    }
                }
            }
        }, KeyCode.UP);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onActionBegin() {
                System.out.println("Action begin");
            }

            @Override
            protected void onAction() {

                if (player.getProperties().getBoolean("canMoveDown")) {
                    //System.out.println(getGameState().getInt("pixelsMovedY") + " : " +  maxBottom);
                    if (getGameState().getInt("pixelsMovedY") >= maxBottom) {
                        maxTop = -sizePlayerY;
                        maxBottom = sizeY;
                        player.setPosition(player.getX(), maxTop);
                        getGameState().setValue("pixelsMovedY", maxTop);
                    } else {
                        player.translateY(vitesse); // move right 5 pixels
                        getGameState().increment("pixelsMovedY", vitesse);
                    }
                }
            }
        }, KeyCode.DOWN);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.JOUEUR, EntityType.POMME) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity joueur, Entity pomme) {
                System.out.println(pomme);
                pomme.removeFromWorld();
                ((Rectangle) player.getView().getNodes().get(0)).setFill(((Circle) pomme.getView().getNodes().get(0)).getFill());
            }
        });
        getPhysicsWorld().addCollisionHandler(new JoueurCollisionHandler(EntityType.JOUEUR, EntityType.MUR) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity joueur, Entity mur) {
                System.out.println("Begin");
            }

            private void goSwitch(Entity joueur, Entity mur, HitBox hitBoxJoueur, HitBox hitBoxMur) {
                int total = 0;

                if (hitBoxJoueur.getMaxXWorld() == hitBoxMur.getMinXWorld()
                        && hitBoxJoueur.getMaxYWorld() > hitBoxMur.getMinYWorld()
                        && hitBoxJoueur.getMinYWorld() < hitBoxMur.getMaxYWorld()){
                    total++;
                    System.out.println("contact coté droit");
                    player.setProperty("canMoveRight", false);
                }

                if (hitBoxJoueur.getMinXWorld() == hitBoxMur.getMaxXWorld()
                        && hitBoxJoueur.getMaxYWorld() > hitBoxMur.getMinYWorld()
                        && hitBoxJoueur.getMinYWorld() < hitBoxMur.getMaxYWorld()){
                    total++;
                    System.out.println("contact coté gauche");
                    player.setProperty("canMoveLeft", false);
                }

                if (hitBoxJoueur.getMinYWorld() == hitBoxMur.getMaxYWorld()
                        && hitBoxJoueur.getMaxXWorld() > hitBoxMur.getMinXWorld()
                        && hitBoxJoueur.getMinXWorld() < hitBoxMur.getMaxXWorld()){
                    total ++;
                    System.out.println("contact coté haut");
                    player.setProperty("canMoveUp", false);
                }

                if (hitBoxJoueur.getMaxYWorld() == hitBoxMur.getMinYWorld()
                        && hitBoxJoueur.getMaxXWorld() > hitBoxMur.getMinXWorld()
                        && hitBoxJoueur.getMinXWorld() < hitBoxMur.getMaxXWorld()){
                    total ++;
                    System.out.println("contact coté bas");
                    player.setProperty("canMoveDown", false);
                };
                System.out.println("Total des contacts = " + total);

                if (total == 1){
                    addMurs(mur, hitBoxMur);
                }
                else {
                    addMurToRemove(mur);
                    System.out.println("Remove " + mur);
                }


            }

            @Override
            protected void onHitBoxTrigger(Entity joueur, Entity mur, HitBox hitBoxJoueur, HitBox hitBoxMur) {
                setHitBoxJoueur(hitBoxJoueur);
                addMurs(mur, hitBoxMur);
                gererLesMurs(joueur, true);
            }

            @Override
            protected void onCollision(Entity joueur, Entity mur) {

            }

            @Override
            protected void onCollisionEnd(Entity joueur, Entity mur) {
                System.out.println("End");
                gererLesMurs(joueur, false);
            }

            private void gererLesMurs(Entity joueur, boolean enchainerAvecEnd){

                player.setProperty("canMoveRight", true);
                player.setProperty("canMoveLeft", true);
                player.setProperty("canMoveUp", true);
                player.setProperty("canMoveDown", true);

                clearMursToRemove();
                System.out.print("Gerer les murs ... ");
                System.out.print(getMurs());
                System.out.println(" Size : " + getMurs().size());
                for (Map.Entry<Entity, HitBox> m : getMurs().entrySet()){
                    goSwitch(joueur, m.getKey(), getHitBoxJoueur(), m.getValue());
                }
                removeAllMurs(getMursToRemove());

                if (enchainerAvecEnd){
                    for (Entity e : getMursToRemove()){
                        onCollisionEnd(joueur, e);
                    }
                }
            }

        });
    }



    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMovedX", sizeX / 2 + 20);
        vars.put("pixelsMovedY", sizeY / 2);
    }

    @Override
    protected void initUI() {
        EnumCouleur[] couleurs = EnumCouleur.values();
        for (int i = 0; i < couleurs.length; i++) {

        }
    }
}