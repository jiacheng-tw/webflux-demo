package com.example.webfluxdemo.user;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class UserValidator {

    public Validation<Seq<String>, User> userValidation(User user) {
        return Validation.combine(
                nameValidation(user.getName()),
                passValidation(user.getPass()))
                .ap(User::new);
    }

    public Validation<String, String> nameValidation(String name) {
        String result = name.replaceAll("[a-zA-Z ]", "");
        return result.isEmpty() ? Validation.valid(name) : Validation.invalid("name error");
    }

    public Validation<String, String> passValidation(String pass) {
        String result = pass.replaceAll("[a-zA-Z ]", "");
        return result.isEmpty() ? Validation.valid(pass) : Validation.invalid("pass error");
    }
}
