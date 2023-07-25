package com.example.webfluxdemo.design.structure.bridge;

public class GreenCircle implements DrawAPI{
    @Override
    public void drawCircle(int x, int y, int radius) {
        System.out.printf("Green circle in (%d, %d) with %d radius...%n", x, y, radius);
    }
}
