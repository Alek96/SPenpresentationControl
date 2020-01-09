package com.zamo.spenserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainClass {
    private static final int PORT = 8000;
    private static final Logger log = LoggerFactory.getLogger(MainClass.class.getSimpleName());

    public static void main(String[] args) {
        log.info("start main");
        Thread server = new Thread(new Server(PORT));
        server.start();
    }
}
