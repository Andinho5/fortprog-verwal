package org.example.ui;

import org.example.user.User;

public abstract class Application implements Screen {

    public Application(User user) {}
    public abstract void logout();

}
