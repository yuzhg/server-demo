package com.demo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void main() throws IOException {
        for (int i = 0; i < 10000; i++) {
            Socket socket = new Socket("localhost", 9000);
            socket.getOutputStream().write("Hello socket".getBytes());
            socket.getOutputStream().flush();
        }
    }
}