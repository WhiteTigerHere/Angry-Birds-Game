//package io.github.badlogic.testgame;
//
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import org.junit.Before;
//import org.junit.Test;
//import com.badlogic.gdx.physics.box2d.World;
//import static org.junit.Assert.assertEquals;
//
//public class BirdTest {
//
//    private Bird bird;
//
//    @Before
//    public void setUp() {
//        // Instead of using a mock, you can create a minimal `World`.
//        World world = new World(new Vector2(0, 0), true);
//        bird = new Bird(world, 5f, 5f, Bird.BirdType.RED);
//    }
//
//    @Test
//    public void testInitialPosition() {
//        assertEquals(new Vector2(5f, 5f), bird.getPosition());
//    }
//
//    @Test
//    public void testGetType() {
//        assertEquals(Bird.BirdType.RED, bird.getType());
//    }
//
//    @Test
//    public void testBodyCreation() {
//        // Ensures body is created and positioned correctly
//        assertEquals(BodyDef.BodyType.DynamicBody, bird.getBody().getType());
//        assertEquals(new Vector2(5f, 5f), bird.getBody().getPosition());
//    }
//}
