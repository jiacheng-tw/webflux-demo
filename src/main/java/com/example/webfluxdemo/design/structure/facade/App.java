package com.example.webfluxdemo.design.structure.facade;

public class App {
    public static void main(String[] args) {
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(new Light(), new Player(), new Projector());
        homeTheater.watchMovie();
        System.out.println("----------");
        homeTheater.stopMovie();
    }
}
