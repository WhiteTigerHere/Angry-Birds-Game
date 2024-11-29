package io.github.badlogic.testgame;

import com.badlogic.gdx.physics.box2d.World;

public interface Savable {
    Object getState(); // Return the state object for saving
    void restoreState(Object state, World world); // Restore from a state object
}
