package org.example;

import java.io.*;

import org.example.ui.MainScreen;
import org.example.ui.Screen;
import org.example.util.Color;

public class Main {

    public static Screen currentScreen;

    public static void main(String[] args) {
        Color.init();
        setScreen(new MainScreen(titleInit()));
    }

    public static void setScreen(Screen screen) {
        currentScreen = screen;
        for(int i = 0; i<20; i++)
            System.out.println();
        currentScreen.onOpen();
    }

    private static int titleInit() {
        int width = 0;
        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("Title");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            while(reader.ready()){
                System.out.println(reader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println();


    }
}