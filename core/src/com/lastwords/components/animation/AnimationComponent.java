package com.lastwords.components.animation;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent implements Component {

    public Animation<TextureRegion> animation;
    public float animationTime;

    public AnimationComponent(Animation<TextureRegion> animation) {
        this(animation, 0f);
    }

    public AnimationComponent(Animation<TextureRegion> animation, float animationTime) {
        this.animation = animation;
        this.animationTime = animationTime;
    }

}
