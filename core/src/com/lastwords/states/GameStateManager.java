package com.lastwords.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lastwords.components.velocity.VelocitySystem;

import java.util.Stack;

public class GameStateManager {

    private Stack<State> states;
    private Engine engine;

    public GameStateManager() {
        this.engine = new Engine();
        this.states = new Stack<State>();
    }

    public void setMainSystems() {
        engine.addSystem(new VelocitySystem());
    }

    public void addEntity(Entity entity) {
        engine.addEntity(entity);
    }

    public void push(State state) {
        states.push(state);
    }

    public void pop() {
        states.pop().dispose();
    }

    public void set(State state) {
        pop();
        states.push(state);
    }

    public void update(float dt) {
        engine.update(dt);
        states.peek().update(dt);
    }

    public void renrer(SpriteBatch spriteBatch) {
        states.peek().render(spriteBatch);
    }

}
