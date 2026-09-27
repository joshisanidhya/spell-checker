package com.shuddhai.api;

import com.shuddhai.spell.SpellChecker;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public final class ApiServer {
    private final HttpServer server;

    public ApiServer(int port, SpellChecker spellChecker) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/check", new RequestHandler(spellChecker));
        server.setExecutor(Executors.newFixedThreadPool(4));
    }

    public void start() { server.start(); }
}