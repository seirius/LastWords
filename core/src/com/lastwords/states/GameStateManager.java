package com.lastwords.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lastwords.ashley.velocity.VelocitySystem;

import java.util.Stack;

public class GameStateManager {

    private Stack<State> states;
    private Engine engine;
    private SpriteBatch spriteBatch;

    public GameStateManager(SpriteBatch spriteBatch) {
        this.engine = new Engine();
        this.states = new Stack<State>();
        this.spriteBatch = spriteBatch;
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
        states.peek().update(dt);
    }

    public void renrer(SpriteBatch spriteBatch) {
        states.peek().render(spriteBatch);
    }

    public Engine getEngine() {
        return engine;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public void resize(int width, int height) {
        states.peek().resize(width, height);
    }
}
