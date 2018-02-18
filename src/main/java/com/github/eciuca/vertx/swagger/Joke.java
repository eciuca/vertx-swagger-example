package com.github.eciuca.vertx.swagger;

public class Joke {

    private final String text;
    private final boolean isFunny;

    public Joke(String text, boolean isFunny) {
        this.text = text;
        this.isFunny = isFunny;
    }

    public String getText() {
        return text;
    }

    public boolean isFunny() {
        return isFunny;
    }
}
