package org.example.ui;

import org.example.user.User;

import java.io.IOException;

public class BankApplication extends Application {
    private User user;

    public BankApplication(User user) {
        super(user);
        this.user = user;
    }

    @Override
    public void onOpen(){
        System.out.println("\n Willkommen in der Banking-App!");
    }

    @Override
    public void takeInput(String string) {

    }


    @Override
    public void logout() {

    }

}
