package com.lastwords.ashley.entities;

import com.badlogic.ashley.core.Component;

import java.util.ArrayList;
import java.util.List;

public class CastComponent implements Component {

    public List<Integer> castPile;

    public CastComponent() {
        castPile = new ArrayList<Integer>();
    }

}
