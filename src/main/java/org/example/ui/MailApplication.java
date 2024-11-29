package org.example.ui;

import org.example.user.User;
import java.io.IOException;

public class MailApplication extends Application {
    public MailApplication(User user) {
        super(user);
    }
    @Override
    public void onOpen(){
        System.out.println("Wilkommen im Postfach");
    }

    @Override
    public void takeInput(String string) {

    }


    @Override
    public void logout() {

    }
}
