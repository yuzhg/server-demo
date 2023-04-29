package com.demo;

import com.demo.netty.NettyServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        log.info("starting the server!");
//        new NioServer().start();
        new NettyServer().start();
    }
}