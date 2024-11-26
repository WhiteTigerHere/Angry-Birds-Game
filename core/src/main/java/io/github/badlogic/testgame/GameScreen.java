package io.github.badlogic.testgame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    private final Core game;
    private String levelFileName;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private World world;
    private Box2DDebugRenderer b2rend;
    private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
    private Level level;
    private Stage stage;
    private Label scoreLabel;
    private int score = 0; // keeps track of the player's score
    public static final float PPM = 100;
    //public int birdcount=3;
    //private OrthographicCamera camera;

    public String getLevelFileName() {
        return levelFileName;
    }

    public int getScore() {
        return score;
    }

    public Core getGame() {
        return game;
    }

    public GameScreen(Core game, String levelFileName) {
        this.game = game;
        this.levelFileName = levelFileName;
        //camera = this.getCamera();


        // Load the map first
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(levelFileName);

        // Get map properties
        int mapWidth = map.getProperties().get("width", Integer.class);
        int mapHeight = map.getProperties().get("height", Integer.class);
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);

        // Calculate world dimensions in meters
        float worldWidth = mapWidth * tileWidth / PPM;
        float worldHeight = mapHeight * tileHeight / PPM;

        // Set up camera and viewport
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(worldWidth, worldHeight, gameCam);
        gameCam.position.set(worldWidth / 2, worldHeight / 2, 0);
        gameCam.update();

        // Create Box2D world
        world = new World(new Vector2(0, -9), true);
        createWorldBounds();
        // Setting Contact Listener
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                Object userDataA = fixtureA.getBody().getUserData();
                Object userDataB = fixtureB.getBody().getUserData();

                Pig pig = null;
                Bird bird = null;
                boolean pigOnGround = false;

                // Determine collision types
                if (userDataA instanceof Pig) {
                    pig = (Pig) userDataA;
                    pigOnGround = "Ground".equals(userDataB);
                } else if (userDataB instanceof Pig) {
                    pig = (Pig) userDataB;
                    pigOnGround = "Ground".equals(userDataA);
                }

                if (userDataA instanceof Bird && userDataB instanceof Pig) {
                    bird = (Bird) userDataA;
                    pig = (Pig) userDataB;
                } else if (userDataB instanceof Bird && userDataA instanceof Pig) {
                    bird = (Bird) userDataB;
                    pig = (Pig) userDataA;
                }

                // Handle pig hitting the bird
                if (pig != null && bird != null) {
                    int damage = bird.getType().getPoints();
                    pig.takeDamage(damage);
                    if (pig.getHealth() <= 0) {
                        pig.burst();
                        score += damage;
                        scoreLabel.setText("Score: " + score);
                    }
                }

                // Handle pig touching the ground
                if (pig != null && pigOnGround) {
                    pig.burst();
                    System.out.println("Pig touched the ground and burst: " + pig);
                }
            }

            @Override
            public void endContact(Contact contact) {}
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });


        Gdx.app.log("GameScreen", "Gravity: " + world.getGravity());
        b2rend = new Box2DDebugRenderer();

        // Create level
        level = Level.createLevel(levelFileName, world,this);
        gameObjects = level.getGameObjects();

        // Set up map renderer
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        // set up stage for ui elements
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        setupUI();
        Gdx.input.setInputProcessor(stage);

        Gdx.app.log("GameScreen", "Level created, map: " + (level.getMap() != null ? "loaded" : "null"));
    }

    public void setInputProcessor() {
        Gdx.input.setInputProcessor(stage);
    }

    private void setupUI() {
        Skin skin=game.skin;

        // create pause button
        Texture pauseTexture = new Texture(Gdx.files.internal("pausebutton.png"));
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseTexture)));

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseScreen(game, GameScreen.this));
            }
        });

        // set up score label
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        scoreLabel = new Label("Score: " + score, labelStyle);

        // set up table for ui layout
        Table table = new Table();
        table.top().left();
        table.setFillParent(true);

        table.add(pauseButton).size(50, 50).padTop(10).padLeft(10);

        table.add().expandX();
        table.add(scoreLabel).right().padTop(10).padRight(10);

        stage.addActor(table);
    }

    @Override
    public void show() {
        setInputProcessor();
        score = ScoreManager.getInstance().getScore();
    }

    private void update(float delta) {
        world.step(1 / 60f, 6, 2);

        Iterator<GameObject> iterator = gameObjects.iterator();
        while (iterator.hasNext()) {
            GameObject obj = iterator.next();
            if (obj instanceof Pig) {
                Pig pig = (Pig) obj;
                if (pig.isMarkedForRemoval()) {
                    world.destroyBody(pig.getBody());
                    int currscore=pig.getType().getPoints();
                    score+=currscore;
                    scoreLabel.setText("Score: " + score);
                    iterator.remove();
                    System.out.println("Pig removed from the game.");

                    char levelno = levelFileName.charAt(5);
                    int levelnum = Character.getNumericValue(levelno);
                    char leveltheme = levelFileName.charAt(6);
                    int levelthemenum = Character.getNumericValue(leveltheme);
                    int levelpointsrequired=0;
                    if(levelnum==1){
                        levelpointsrequired=2000;
                    }
                    else if(levelnum==2){
                        levelpointsrequired=6000;
                    }
                    else if(levelnum==3){
                        levelpointsrequired=12000;
                    }

                    // Check for Level Win condition
                    if (score >= levelpointsrequired ) {
                        // Transition to Level Win screen
                        if(levelnum==1){
                            score+=600;
                        }
                        else if(levelnum==2){
                            score+=1600;
                        }
                        else if(levelnum==3){
                            score+=4400;
                        }
                        // Use a postRunnable to delay the screen change
                        Gdx.app.postRunnable(() -> {
                            // Add a delay before switching screens
                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    game.setScreen(new LevelWin(game, "Red", score, levelnum, levelthemenum));  // Transition with updated score
                                }
                            }, 2f);  // 2 seconds delay
                        });
                    }
                }
            }

            // Handle updates for other game objects, e.g., slingshot movements.
            if (obj instanceof Slingshot) {
                ((Slingshot) obj).update(delta);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);


        mapRenderer.setView(gameCam);
        mapRenderer.render();

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        for (GameObject gameObject : gameObjects) {
            gameObject.draw(game.batch);
        }
        game.batch.end();

        b2rend.render(world, gameCam.combined);

        // Render slingshot string and trajectory
        //Slingshot.getInstance(world, 135.33f/ GameScreen.PPM, 315.33f / GameScreen.PPM, 50 / GameScreen.PPM, 100 / GameScreen.PPM, camera).renderString();
        //Slingshot.getInstance(world, 135.33f/ GameScreen.PPM, 315.33f / GameScreen.PPM, 50 / GameScreen.PPM, 100 / GameScreen.PPM, camera).renderTrajectory();

        stage.act(delta);
        stage.draw();
    }
    private void createWorldBounds() {
        // Define a static body for the walls
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // Define a shape for the walls
        EdgeShape edgeShape = new EdgeShape();

        // Create fixtures for the walls
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = edgeShape;
        fixtureDef.friction = 0.5f;

        // Left wall
        bodyDef.position.set(0, 0); // Left edge of the world
        Body leftWall = world.createBody(bodyDef);
        edgeShape.set(new Vector2(0, 0), new Vector2(0, gamePort.getWorldHeight()));
        leftWall.createFixture(fixtureDef);

        // Right wall
        bodyDef.position.set(gamePort.getWorldWidth(), 0); // Right edge of the world
        Body rightWall = world.createBody(bodyDef);
        edgeShape.set(new Vector2(0, 0), new Vector2(0, gamePort.getWorldHeight()));
        rightWall.createFixture(fixtureDef);

        // Dispose of the shape after creating fixtures
        edgeShape.dispose();
    }



    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        // dispose of resources
        mapRenderer.dispose();
        map.dispose();
        stage.dispose();
        b2rend.dispose();
        for (GameObject gameObject : gameObjects) {
            gameObject.getTexture().dispose();
        }
    }

    OrthographicCamera getCamera(){
        return gameCam;
    }


}
