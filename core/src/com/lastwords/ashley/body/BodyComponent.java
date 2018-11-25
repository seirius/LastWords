package com.lastwords.ashley.body;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class BodyComponent implements Component {

    private Body body;
    private BodyDef bodyDef;
    private Fixture fixture;

    private Vector2 initPosition;
    private BodyDef.BodyType bodyType;
    private Shape shape;

    public BodyComponent(Vector2 initPosition, BodyDef.BodyType bodyType, Shape shape) {
        this.initPosition = initPosition;
        this.bodyType = bodyType;
        this.shape = shape;
    }

    public void initBody(World world) {
        bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyType = null;
        bodyDef.position.set(initPosition.cpy());
        initPosition = null;

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;

        fixture = body.createFixture(fixtureDef);

        shape.dispose();
        shape = null;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }
}
