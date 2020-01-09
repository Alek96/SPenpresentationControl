package com.zamo.spenserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainClass {
    private static final int PORT = 1234;
    private static final Logger log = LoggerFactory.getLogger(MainClass.class.getSimpleName());

    public static void main(String[] args) {
        log.info("start main");
        Server server = new Server(PORT);
        Thread serverThread = new Thread(server);
        serverThread.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            reader.readLine();
            server.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
