package org.example.ui;

import org.example.user.User;

import java.io.IOException;

public abstract class Application implements Screen {

    public Application(User user) {}
    public abstract void logout() throws IOException;

}
