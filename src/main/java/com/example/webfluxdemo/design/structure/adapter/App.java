package com.example.webfluxdemo.design.structure.adapter;

public class App {
    public static void main(String[] args) {
        Mobile mobile = new Mobile();
        V5PowerAdaptor v5PowerAdaptor = new V5PowerAdaptor(new V220Power());
        mobile.charge(v5PowerAdaptor);
    }
}
