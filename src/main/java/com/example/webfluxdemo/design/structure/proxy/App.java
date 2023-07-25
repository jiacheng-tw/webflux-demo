package com.example.webfluxdemo.design.structure.proxy;

public class App {
    public static void main(String[] args) {
        Image image = new ProxyImage("image_file");
        image.display();
    }
}
