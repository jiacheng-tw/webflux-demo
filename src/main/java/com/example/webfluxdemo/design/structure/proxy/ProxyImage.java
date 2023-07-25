package com.example.webfluxdemo.design.structure.proxy;

public class ProxyImage implements Image{
    private String fileName;
    private RealImage realImage;

    public ProxyImage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if (realImage == null) {
            this.realImage = new RealImage(fileName);
            System.out.println("Creating image " + fileName);
        }
        realImage.load();
        realImage.display();
    }
}
