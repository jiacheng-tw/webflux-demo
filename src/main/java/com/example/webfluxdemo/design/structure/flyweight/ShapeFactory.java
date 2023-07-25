package com.example.webfluxdemo.design.structure.flyweight;

import java.util.HashMap;

public class ShapeFactory {
    private static final HashMap<String, Shape> SHAPE_MAP = new HashMap<>();

    public static Shape getCircle(String color) {
        Point point = (Point) SHAPE_MAP.get(color);

        if (point == null) {
            point = new Point(color);
            SHAPE_MAP.put(color, point);
            System.out.printf("Creating %s point...%n", color);
        }
        return point;
    }
}
