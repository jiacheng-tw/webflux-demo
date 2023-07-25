package com.example.webfluxdemo.design.structure.composite;

public class App {
    public static void main(String[] args) {
        Employee xiaoMing = new Employee("Xiao Ming", "LV 3");
        Employee xiaoLi = new Employee("Xiao Li", "LV 2");
        Employee xiaoLiang = new Employee("Xiao Liang", "LV 1");

        xiaoMing.add(xiaoLi);
        xiaoLi.add(xiaoLiang);

        System.out.println(xiaoMing);
        for (Employee e1 : xiaoMing.getSubordinates()) {
            System.out.println(" " + e1);
            for (Employee e2 : e1.getSubordinates()) {
                System.out.println("  " + e2);
            }
        }
    }
}
