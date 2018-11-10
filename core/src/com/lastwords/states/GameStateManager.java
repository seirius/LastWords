package com.lastwords.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GameStateManager {

    private Stack<State> states;

    public GameStateManager() {
        states = new Stack<State>();
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

}
