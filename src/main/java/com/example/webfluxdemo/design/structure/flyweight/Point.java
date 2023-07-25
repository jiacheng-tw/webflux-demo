package com.example.webfluxdemo.design.structure.flyweight;

public class Point implements Shape{
    private String color;

    public Point(String color) {
        this.color = color;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a point with color " + color);
    }
}
