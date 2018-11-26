package com.lastwords.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.lastwords.ashley.animation.AnimationComponent;
import com.lastwords.ashley.body.BodyComponent;
import com.lastwords.ashley.entities.CastComponent;
import com.lastwords.ashley.entities.EntityStateComponent;
import com.lastwords.ashley.playerinput.PlayerComponent;
import com.lastwords.ashley.position.PositionComponent;
import com.lastwords.ashley.stats.StatsComponent;
import com.lastwords.ashley.texture.TextureComponent;
import com.lastwords.ashley.velocity.VelocityComponent;
import com.lastwords.ashley.world.AddToWorldComponent;

public class AshleyEntity extends Entity {

    private static final int FRAME_COLS = 9, FRAME_ROWS = 2;

    public AshleyEntity(float xPosition, float yPosition, float speed) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(6);
        add(new BodyComponent(
                new Vector2(xPosition, yPosition),
                BodyDef.BodyType.DynamicBody, circleShape
        ));
        add(new CastComponent());
        add(new AddToWorldComponent());
        add(new PositionComponent(xPosition, yPosition));
        add(new VelocityComponent());
        add(new EntityStateComponent());
        StatsComponent statsComponent = new StatsComponent();
        statsComponent.speed = speed;
        add(statsComponent);
        add(new PlayerComponent());


        Texture texture = new Texture("micro/PNG/Human/human_regular_bald.png");
        TextureRegion[][] tmp = TextureRegion
                .split(texture, texture.getWidth() / FRAME_COLS, texture.getHeight() / FRAME_ROWS);
        TextureRegion[] textureRegions = {tmp[0][0], tmp[0][1], tmp[0][2]};
        add(new AnimationComponent(new Animation<TextureRegion>(0.2f, textureRegions)));
        add(new TextureComponent());
    }

}
