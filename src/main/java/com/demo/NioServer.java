package com.demo;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

@Slf4j
public class NioServer {
    public void start() throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(9000));
        serverSocket.configureBlocking(false);
        log.info("server start at port 9000");

        Selector selector = Selector.open();
        try {
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                // use the default thread pool to handle the sockets that have new messages arrived.
                selectionKeys.stream().parallel().forEach(k -> {
                    try {
                        if (k.isAcceptable()) {
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) k.channel();
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            log.info("new connections created! current connections number is {}", selector.keys().size());
                        } else if (k.isReadable()) {
                            // java NIO by default using the level triggering, so for each reading, just
                            // read out a predefined sized(256 here) data.
                            SocketChannel socketChannel = (SocketChannel) k.channel();
                            ByteBuffer buf = ByteBuffer.allocate(256);
                            int len = socketChannel.read(buf);
                            if (len > 0) {
                                log.info("accepted msg: " + new String(buf.array()));
                            } else if (len == -1) {
                                log.info("socket closed");
                                socketChannel.close();
                            }
                        }
                    } catch (IOException e) {
                        log.error("something goes wrong when handle the channels: ", e);
                    }
                });
                selectionKeys.clear();
            }
        } catch(Exception e) {
            log.error("error happens: ", e);
        }
        finally {
            selector.close();
        }
    }
}
