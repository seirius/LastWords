package com.lastwords.ashley.texture;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent implements Component {

    public TextureRegion textureRegion;

    public TextureComponent() {}

    public TextureComponent(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

}
