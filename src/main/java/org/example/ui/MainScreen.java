package org.example.ui;

import org.example.Main;

public class MainScreen implements Screen {
    int width;


    public MainScreen(int width) {
        this.width = width;
    }

    @Override
    public void onOpen() {
        System.out.println("öüä");
        centerText("Willkommen im Hauptmenü vom BalanceBuddy!");
        centerText("Melde dich an");

    }

    @Override
    public void takeInput(String string) {

    }

    public void centerText(String text) {
        int padding = (width - text.length()) / 2;
        String format = "%" + (padding + text.length()) + "s";
        //System.out.println(String.format(format, text).concat(" ".repeat(width - padding - text.length())));
        System.out.println(text);
    }
}
