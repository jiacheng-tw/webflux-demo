package com.example.webfluxdemo.design.structure.bridge;

public class RedCircle implements DrawAPI{
    @Override
    public void drawCircle(int x, int y, int radius) {
        System.out.printf("Red circle in (%d, %d) with %d radius...%n", x, y, radius);
    }
}
