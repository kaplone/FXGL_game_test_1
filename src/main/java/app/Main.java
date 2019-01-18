package app;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.time.Timer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.But;
import models.Item;
import models.Labyrinthe;
import models.Niveau;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static app.EntityType.*;
import static app.EnumDirection.NONE;

public class Main extends GameApplication {

    private Entity player;
    private List<Entity> murs;
    private List<Entity> pommes;
    private Entity pomme;
    private GameSettings settings;
    private Integer sizeX = 800;
    private Integer sizeY = 600;
    private Integer sizePlayerX = 30;
    private Integer sizePlayerY = 30;
    private Double maxLeft;
    private Double maxRight;
    private Double maxTop;
    private Double maxBottom;
    private Double vitesse;
    private Text chrono1;
    private Text chrono2;
    private Text chrono3;
    private Text chrono4;
    private VBox chronos;

    private DecimalFormat df;

    private Niveau niveau;
    private String[] keys;
    private Text[] chronosTexts;

    private Double chronoStart;
    private Boolean isChronoStarted;

    private Entity item;
    private List<Entity> items;

    private Timer timer;

    @Override
    protected void initSettings(GameSettings settings) {
        this.settings = settings;
        settings.setWidth(sizeX);
        settings.setHeight(sizeY);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");
        //settings.setProfilingEnabled(true);
        isChronoStarted = false;
    }

    @Override
    protected void initGame() {

        vitesse = 0.5d;

        player = Entities.builder()
                .type(JOUEUR)
                .at(20, 20)
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
        items = new ArrayList<>();

        niveau = new Niveau();

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            niveau = mapper.readValue(new File("src/main/resources/niveaux/niveau_01.yml"), Niveau.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Item item_ : niveau.getItems()) {
            item = Entities.builder()
                    .type(item_.getType())
                    .at(item_.getxPos(), item_.getyPos())
                    .viewFromTextureWithBBox(item_.getImagePath())
                    .with(new CollidableComponent(true), new PhysicsComponent())
                    .buildAndAttach(getGameWorld());
            items.add(item);
        }

        murs = Labyrinthe.getMurs(niveau.getDessin(), getGameWorld());
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        maxRight = sizeX.doubleValue();
        maxLeft = -sizePlayerX.doubleValue();
        maxTop = -sizePlayerY.doubleValue();
        maxBottom = sizeY.doubleValue();

        input.addAction(new UserAction("Clic") {
            @Override
            protected void onAction() {
                player.setPosition(20, 20);
                getGameState().setValue("pixelsMovedX", 20);
                getGameState().setValue("pixelsMovedY", 20);
            }
        }, MouseButton.PRIMARY);

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                if (!isChronoStarted) {
                    isChronoStarted = true;
                    chronoStart = timer.getNow();
                }
                //System.out.println("Action begin");
            }

            @Override
            protected void onAction() {

                if (player.getProperties().getBoolean("canMoveRight")) {
                    // si l'objet sort par la droite il rentre par la gauche
                    if (getGameState().getDouble("pixelsMovedX") >= maxRight) {
                        maxLeft = -sizePlayerX.doubleValue();
                        maxRight = sizeX.doubleValue();
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
                if (!isChronoStarted) {
                    isChronoStarted = true;
                    chronoStart = timer.getNow();
                }
                //System.out.println("Action begin");
            }

            @Override
            protected void onAction() {

                if (player.getProperties().getBoolean("canMoveLeft")) {
                    // si l'objet sort par la gauche il rentre par a droite
                    //System.out.println(getGameState().getInt("pixelsMovedX") + " : " +  maxLeft);
                    if (getGameState().getDouble("pixelsMovedX") <= maxLeft) {
                        maxLeft = -sizePlayerX.doubleValue();
                        maxRight = sizeX.doubleValue();
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
                if (!isChronoStarted) {
                    isChronoStarted = true;
                    chronoStart = timer.getNow();
                }
                //System.out.println("Action begin");
            }

            @Override
            protected void onAction() {

                if (player.getProperties().getBoolean("canMoveUp")) {
                    // si l'objet sort par le haut il rentre par le bas
                    // System.out.println(getGameState().getInt("pixelsMovedY") + " : " +  maxTop);
                    if (getGameState().getDouble("pixelsMovedY") <= maxTop) {
                        maxTop = -sizePlayerY.doubleValue();
                        maxBottom = sizeY.doubleValue();
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
                if (!isChronoStarted) {
                    isChronoStarted = true;
                    chronoStart = timer.getNow();
                }
                //System.out.println("Action begin");
            }

            @Override
            protected void onAction() {

                if (player.getProperties().getBoolean("canMoveDown")) {
                    //System.out.println(getGameState().getInt("pixelsMovedY") + " : " +  maxBottom);
                    if (getGameState().getDouble("pixelsMovedY") >= maxBottom) {
                        maxTop = -sizePlayerY.doubleValue();
                        maxBottom = sizeY.doubleValue();
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
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(JOUEUR, POMME) {

            @Override
            protected void onCollisionBegin(Entity joueur, Entity pomme) {
                pomme.removeFromWorld();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(JOUEUR, CACTUS) {
 
            @Override
            protected void onCollisionBegin(Entity joueur, Entity cactus) {
                ((Rectangle) player.getView().getNodes().get(0)).setFill(Color.RED);
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(JOUEUR, CLE) {

            @Override
            protected void onCollisionBegin(Entity joueur, Entity cle) {
                cle.removeFromWorld();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(JOUEUR, HACHE) {

            @Override
            protected void onCollisionBegin(Entity joueur, Entity hache) {
                hache.removeFromWorld();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(JOUEUR, MUR) {

            @Override
            protected void onCollisionBegin(Entity joueur, Entity mur) {
            }


            @Override
            protected void onHitBoxTrigger(Entity joueur, Entity mur, HitBox hitBoxJoueur, HitBox hitBoxMur) {
            }

            @Override
            protected void onCollision(Entity joueur, Entity mur) {

            }

            @Override
            protected void onCollisionEnd(Entity joueur, Entity mur) {
            }
        });
    }


    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMovedX", 20d);
        vars.put("pixelsMovedY", 20d);

        timer = getMasterTimer();
    }

    @Override
    protected void initUI() {

        df = new DecimalFormat("00.00");

        chronos = new VBox();
        chronos.setSpacing(5);
        chronos.setStyle("-fx-padding: 3;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: blue;");
        chronos.setTranslateX(650); // x = 700
        chronos.setTranslateY(100); // y = 100

        chronosTexts = new Text[]{chrono1, chrono2, chrono3, chrono4};

        Set<String> niveaux = niveau.getCibles().stream().map(a -> a.getImagePath()).collect(Collectors.toSet());
        //dissocier les cibles et les niveaux

        for (int i = 0; i < niveaux.size(); i++) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setSpacing(20);
            hbox.setStyle("-fx-padding: 2;" + "-fx-border-style: solid inside;"
                    + "-fx-border-width: 1;" + "-fx-border-insets: 5;"
                    + "-fx-border-radius: 5;" + "-fx-border-color: black;");
            chronosTexts[i] = new Text();
            chronosTexts[i].setText(df.format(niveau.getCibles().get(i).getTempsMax()));
            ImageView imageView = new ImageView("assets/textures/" + niveau.getCibles().get(i).getImagePath());
            hbox.getChildren().addAll(imageView, chronosTexts[i]);
            chronos.getChildren().addAll(hbox);
        }

        chrono1 = chronosTexts[0];
        chrono2 = chronosTexts[1];
        chrono3 = chronosTexts[2];
        chrono4 = chronosTexts[3];

        System.out.println(chrono1);

        getGameScene().addUINode(chronos); // add to the scene graph
    }

    protected void gererLesContacts(Entity player, Entity mur, HitBox hitBoxJoueur, HitBox hitBoxMur) {

        //System.out.println(hitBoxMur);

        int total = 0;
        Boolean r = true;
        Boolean l = true;
        Boolean u = true;
        Boolean d = true;

        if (hitBoxJoueur.getMaxXWorld() == hitBoxMur.getMinXWorld()
                && hitBoxJoueur.getMaxYWorld() > hitBoxMur.getMinYWorld()
                && hitBoxJoueur.getMinYWorld() < hitBoxMur.getMaxYWorld()) {
            total++;
            //System.out.println("contact coté droit");
            r = false;
        }

        if (hitBoxJoueur.getMinXWorld() == hitBoxMur.getMaxXWorld()
                && hitBoxJoueur.getMaxYWorld() > hitBoxMur.getMinYWorld()
                && hitBoxJoueur.getMinYWorld() < hitBoxMur.getMaxYWorld()) {
            total++;
            //System.out.println("contact coté gauche");
            l = false;
        }

        if (hitBoxJoueur.getMinYWorld() == hitBoxMur.getMaxYWorld()
                && hitBoxJoueur.getMaxXWorld() > hitBoxMur.getMinXWorld()
                && hitBoxJoueur.getMinXWorld() < hitBoxMur.getMaxXWorld()) {
            total++;
            //System.out.println("contact coté haut");
            u = false;
        }

        if (hitBoxJoueur.getMaxYWorld() == hitBoxMur.getMinYWorld()
                && hitBoxJoueur.getMaxXWorld() > hitBoxMur.getMinXWorld()
                && hitBoxJoueur.getMinXWorld() < hitBoxMur.getMaxXWorld()) {
            total++;
            //System.out.println("contact coté bas");
            d = false;
        }
        ;
        //System.out.println("Total des contacts = " + total);

        if (total == 1) {
            if (!r) {
                player.setProperty("canMoveRight", false);
            } else if (!l) {
                player.setProperty("canMoveLeft", false);
            } else if (!u) {
                player.setProperty("canMoveUp", false);
            } else if (!d) {
                player.setProperty("canMoveDown", false);
            }
        }
    }

    @Override
    protected void onUpdate(double tpf) {


        if (isChronoStarted) {
            chrono1.setText(df.format((niveau.getCibles().get(0).getTempsMax() + chronoStart / 3 - timer.getNow() / 3)));
        }


        player.setProperty("canMoveRight", true);
        player.setProperty("canMoveLeft", true);
        player.setProperty("canMoveUp", true);
        player.setProperty("canMoveDown", true);

        Labyrinthe.getMursHit().entrySet().parallelStream()
                .filter(k -> Math.abs(player.getX() - k.getKey().getX()) <= 60
                        && Math.abs(player.getY() - k.getKey().getY()) <= 60)
                .forEach(m -> gererLesContacts(player, m.getKey(), player.getBoundingBoxComponent().hitBoxesProperty().get(0), m.getValue().getHitBox()));

        items.stream()
                .filter(a -> CACTUS.equals(a.getType()))
                .forEach(i -> gererLesContacts(player, i, player.getBoundingBoxComponent().hitBoxesProperty().get(0), i.getBoundingBoxComponent().hitBoxesProperty().get(0)));
    }
}