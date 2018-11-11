package com.lastwords.components.position;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {

    public Vector2 position;

    public PositionComponent(float xPosition, float yPosition) {
        this.position = new Vector2(xPosition, yPosition);
    }

}
