package com.example.webfluxdemo.design.structure.bridge;

public class App {
    public static void main(String[] args) {
        int x =10;
        int y = 12;
        int radius = 6;

        Shape redCircle = new Circle(new RedCircle(), x, y, radius);
        redCircle.draw();

        Shape greedCircle = new Circle(new GreenCircle(), x, y, radius);
        greedCircle.draw();
    }
}
