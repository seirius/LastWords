package com.lastwords.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player {

    private Vector2 position;
    private float velocity;
    private Texture texture;
    private TextureRegion[] textureRegions;
    private TextureRegion textureRegion;
    private Animation<TextureRegion> animation;
    private float stateTime;

    private static final int FRAME_COLS = 9, FRAME_ROWS = 2;

    public Player(float xPosition, float yPosition, float velocity) {
        this.position = new Vector2(xPosition, yPosition);
        this.velocity = velocity;
        this.texture = new Texture("micro/PNG/Human/human_regular_bald.png");
        TextureRegion[][] tmp = TextureRegion
                .split(this.texture, this.texture.getWidth() / FRAME_COLS, this.texture.getHeight() / FRAME_ROWS);
        this.textureRegions = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                this.textureRegions[index++] = tmp[i][j];
            }
        }
        this.animation = new Animation<TextureRegion>(0.2f, this.textureRegions);
        this.stateTime = 0f;
    }

    public void render(SpriteBatch spriteBatch) {
//        spriteBatch.draw(texture, position.x - 10, position.y - 10, 0, 20, 20, 20);
        if (textureRegion != null) {
            int regionWidth = textureRegion.getRegionWidth(), regionHeight = textureRegion.getRegionHeight();
            spriteBatch.draw(textureRegion, position.x - regionWidth / 2f, position.y - regionHeight / 2f, regionWidth, regionHeight);
        }
    }

    public void update(float dt) {
        this.stateTime += dt;
        textureRegion = animation.getKeyFrame(this.stateTime);
        if (animation.isAnimationFinished(this.stateTime)) {
            this.stateTime = 0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.add(new Vector2(-1, 0).scl(velocity).scl(dt));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.add(new Vector2(0, 1).scl(velocity).scl(dt));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.add(new Vector2(1, 0).scl(velocity).scl(dt));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.add(new Vector2(0, -1).scl(velocity).scl(dt));
        }
    }

    public void dispose() {
        texture.dispose();
    }


}
