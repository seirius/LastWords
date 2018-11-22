package com.lastwords.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lastwords.components.animation.AnimationComponent;
import com.lastwords.components.playerinput.PlayerComponent;
import com.lastwords.components.position.PositionComponent;
import com.lastwords.components.stats.StatsComponent;
import com.lastwords.components.texture.TextureComponent;
import com.lastwords.components.velocity.VelocityComponent;

public class AshleyEntity extends Entity {

    private static final int FRAME_COLS = 9, FRAME_ROWS = 2;

    public AshleyEntity(float xPosition, float yPosition, float velocity) {
        this.add(new PositionComponent(xPosition, yPosition));
        this.add(new VelocityComponent());
        StatsComponent statsComponent = new StatsComponent();
        statsComponent.speed = 60;
        this.add(statsComponent);
        this.add(new PlayerComponent());


        Texture texture = new Texture("micro/PNG/Human/human_regular_bald.png");
        TextureRegion[][] tmp = TextureRegion
                .split(texture, texture.getWidth() / FRAME_COLS, texture.getHeight() / FRAME_ROWS);
        TextureRegion[] textureRegions = {tmp[0][0], tmp[0][1], tmp[0][2]};
        this.add(new AnimationComponent(new Animation<TextureRegion>(0.2f, textureRegions)));
        this.add(new TextureComponent());
    }

}