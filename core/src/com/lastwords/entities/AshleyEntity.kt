package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.entities.CastComponent
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.playerinput.PlayerComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.AddToWorldComponent

class AshleyEntity(xPosition: Float, yPosition: Float, speed: Float) : Entity() {

    init {
        val circleShape = CircleShape()
        circleShape.radius = 6f
        add(BodyComponent(
                Vector2(xPosition, yPosition),
                BodyDef.BodyType.DynamicBody, circleShape
        ))
        add(CastComponent())
        add(AddToWorldComponent())
        add(PositionComponent(xPosition, yPosition))
        add(VelocityComponent())
        add(EntityStateComponent())
        val statsComponent = StatsComponent()
        statsComponent.speed = speed
        add(statsComponent)
        add(PlayerComponent())


        //        Texture texture = new Texture("micro/PNG/Human/human_regular_bald.png");
        //        TextureRegion[][] tmp = TextureRegion
        //                .split(texture, texture.getWidth() / FRAME_COLS, texture.getHeight() / FRAME_ROWS);
        //        TextureRegion[] textureRegions = {tmp[0][0], tmp[0][1], tmp[0][2]};
        //        add(new AnimationComponent(new Animation<TextureRegion>(0.2f, textureRegions)));
        //        add(new TextureComponent());
    }

    companion object {

        private val FRAME_COLS = 9
        private val FRAME_ROWS = 2
    }

}
