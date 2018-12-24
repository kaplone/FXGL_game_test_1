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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static app.EnumDirection.*;

public class Main extends GameApplication {

    private Entity player;
    private List<Entity> pommes;
    private List<Entity> murs;
    private Entity pomme;
    private Entity mur;
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

        murs = new ArrayList<>();
        mur = Entities.builder()
                .type(EntityType.MUR)
                .at(40, 50)
                .viewFromNodeWithBBox(new Rectangle(10, 225, Color.BLACK))
                .with(new CollidableComponent(true), new PhysicsComponent())
                .buildAndAttach(getGameWorld());
        murs.add(mur);
        mur = Entities.builder()
                .type(EntityType.MUR)
                .at(40, 325)
                .viewFromNodeWithBBox(new Rectangle(10, 225, Color.BLACK))
                .with(new CollidableComponent(true), new PhysicsComponent())
                .buildAndAttach(getGameWorld());
        murs.add(mur);
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
            protected  void onActionBegin(){
                player.setProperty("prevDirection", player.getObject("nextDirection"));
            }

            @Override
            protected void onAction() {
                player.setProperty("nextDirection", RIGHT);
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
                } else {
                    player.setProperty("canMoveLeft", true);
                    player.setProperty("canMoveUp", true);
                    player.setProperty("canMoveDown", true);
                    if ("normal".equals(player.getString("Etat"))) {
                        player.setProperty("canMoveRight", true);
                    }
                }
            }
        }, KeyCode.RIGHT);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected  void onActionBegin(){
                player.setProperty("prevDirection", player.getObject("nextDirection"));
            }

            @Override
            protected void onAction() {
                player.setProperty("nextDirection", LEFT);
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
                } else {
                    player.setProperty("canMoveRight", true);
                    player.setProperty("canMoveUp", true);
                    player.setProperty("canMoveDown", true);
                    if ("normal".equals(player.getString("Etat"))) {
                        player.setProperty("canMoveLeft", true);
                    }
                }
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected  void onActionBegin(){
                player.setProperty("prevDirection", player.getObject("nextDirection"));
            }

            @Override
            protected void onAction() {
                player.setProperty("nextDirection", UP);
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
                } else {
                    player.setProperty("canMoveRight", true);
                    player.setProperty("canMoveLeft", true);
                    player.setProperty("canMoveDown", true);
                    if ("normal".equals(player.getString("Etat"))) {
                        player.setProperty("canMoveUp", true);
                    }
                }
            }
        }, KeyCode.UP);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected  void onActionBegin(){
                player.setProperty("prevDirection", player.getObject("nextDirection"));
            }

            @Override
            protected void onAction() {
                player.setProperty("nextDirection", DOWN);
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
                } else {
                    player.setProperty("canMoveRight", true);
                    player.setProperty("canMoveLeft", true);
                    player.setProperty("canMoveUp", true);
                    if ("normal".equals(player.getString("Etat"))) {
                        player.setProperty("canMoveDown", true);
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

            @Override
            protected void onHitBoxTrigger(Entity joueur, Entity mur, HitBox hitBoxJoueur, HitBox hitBoxMur) {
                System.out.println("Trigger");
                player.setProperty("Etat", "preEnd");

                if ((RIGHT.equals(player.getObject("nextDirection")) || RIGHT.equals(player.getObject("prevDirection")))
                        && hitBoxJoueur.getMaxXWorld() >= hitBoxMur.getMinXWorld()
                        && !(hitBoxJoueur.getMinYWorld() >= hitBoxMur.getMaxYWorld()
                        || hitBoxJoueur.getMaxYWorld() <= hitBoxMur.getMinYWorld())) {
                    System.out.println("contact coté droit");
                    player.setProperty("canMoveRight", false);
                }
                if ((LEFT.equals(player.getObject("nextDirection")) || LEFT.equals(player.getObject("prevDirection")))
                        && hitBoxJoueur.getMinXWorld() <= hitBoxMur.getMaxXWorld()
                        && !(hitBoxJoueur.getMinYWorld() >= hitBoxMur.getMaxYWorld()
                        || hitBoxJoueur.getMaxYWorld() <= hitBoxMur.getMinYWorld())) {
                    System.out.println("contact coté gauche");
                    player.setProperty("canMoveLeft", false);
                }
                if ((UP.equals(player.getObject("nextDirection")) || UP.equals(player.getObject("prevDirection")))
                        && hitBoxJoueur.getMinYWorld() >= hitBoxMur.getMaxYWorld()
                        && !(hitBoxJoueur.getMinXWorld() >= hitBoxMur.getMaxXWorld()
                        || hitBoxJoueur.getMaxXWorld() <= hitBoxMur.getMinXWorld())) {
                    System.out.println("contact coté haut");
                    player.setProperty("canMoveUp", false);
                }
                if ((DOWN.equals(player.getObject("nextDirection")) || DOWN.equals(player.getObject("prevDirection")))
                        && hitBoxJoueur.getMaxYWorld() <= hitBoxMur.getMinYWorld()
                        && !(hitBoxJoueur.getMinXWorld() >= hitBoxMur.getMaxXWorld()
                        || hitBoxJoueur.getMaxXWorld() <= hitBoxMur.getMinXWorld())) {
                    System.out.println("contact coté bas");
                    player.setProperty("canMoveDown", false);
                }
            }

            @Override
            protected void onCollision(Entity joueur, Entity mur) {
                player.setProperty("Etat", "preEnd");
                //onCollisionPostEnd(player, mur);
            }

            @Override
            protected void onCollisionEnd(Entity joueur, Entity mur) {
                System.out.println("End");
                if (!"preEnd".equals(player.getString("Etat"))) {
                    onCollisionPostEnd(player, mur);
                } else {
                    player.setProperty("Etat", "normal");
                }

            }

            protected void onCollisionPreEnd(Entity joueur, Entity mur) {

            }

            protected void onCollisionPostEnd(Entity joueur, Entity mur) {
                player.setProperty("canMoveRight", true);
                player.setProperty("canMoveLeft", true);
                player.setProperty("canMoveUp", true);
                player.setProperty("canMoveDown", true);
            }
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMovedX", sizeY / 2);
        vars.put("pixelsMovedY", sizeY / 2);
    }

    @Override
    protected void initUI() {
        EnumCouleur[] couleurs = EnumCouleur.values();
        for (int i = 0; i < couleurs.length; i++) {

        }

        /*Text textPixelsX = new Text();
        Text textPixelsY = new Text();
        textPixelsX.setTranslateX(50); // x = 50
        textPixelsY.setTranslateX(90); // x = 50
        textPixelsX.setTranslateY(100); // y = 100
        textPixelsY.setTranslateY(100); // y = 100
        textPixelsX.textProperty().bind(getGameState().intProperty("pixelsMovedX").asString());
        textPixelsY.textProperty().bind(getGameState().intProperty("pixelsMovedY").asString());

        getGameScene().addUINode(textPixelsX); // add to the scene graph
        getGameScene().addUINode(textPixelsY); // add to the scene graph*/

    }
}