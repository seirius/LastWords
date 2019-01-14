package com.lastwords.states

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.lastwords.ashley.velocity.VelocitySystem

import java.util.Stack

class GameStateManager(var spriteBatch: SpriteBatch) {

    val states: Stack<State> = Stack()
    val engine: Engine = Engine()

    fun setMainSystems() {
        engine.addSystem(VelocitySystem())
    }

    fun addEntity(entity: Entity) {
        engine.addEntity(entity)
    }

    fun push(state: State) {
        states.push(state)
    }

    fun pop() {
        states.pop().dispose()
    }

    fun set(state: State) {
        pop()
        states.push(state)
    }

    fun update(dt: Float) {
        states.peek().update(dt)
    }

    fun render(spriteBatch: SpriteBatch) {
        states.peek().render(spriteBatch)
    }

    fun resize(width: Int, height: Int) {
        states.peek().resize(width, height)
    }
}
