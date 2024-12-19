package org.example.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public interface Screen {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    void onOpen() throws IOException;
    void listUsers();
    void logout() throws IOException;
}
