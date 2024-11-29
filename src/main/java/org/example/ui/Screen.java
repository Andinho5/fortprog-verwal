package org.example.ui;

import java.io.IOException;

public interface Screen {
    void onOpen() throws IOException;
    void takeInput(String string);
}
