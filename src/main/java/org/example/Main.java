package org.example;

import java.io.*;

import org.example.ui.MainScreen;
import org.example.ui.Screen;
import org.example.util.Color;

public class Main {
    //TODO exceptions nicht weitergeben sondern direkt handlen
    public static Screen currentScreen;

    public static void main(String[] args) throws IOException {
        Color.init();
        setScreen(new MainScreen(titleInit()));
    }

    public static void setScreen(Screen screen) throws IOException {
        if (currentScreen != null) {
            for(int i = 0; i<20; i++)
                System.out.println();
        }
        currentScreen = screen;
        currentScreen.onOpen();
    }

    private static int titleInit() {
        int width = 0;
        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("Title");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            while(reader.ready()){
                System.out.println(Color.RED + reader.readLine() + Color.RESET);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
        UserService userService = new UserService();
        userService.save(new User("7wd8a7wd", "pablo@gmail.com", "pablo123", 10000));
        return width;
    }
}