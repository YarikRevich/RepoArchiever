package com.repoachiever.service.telemetry;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.net.Socket;

/**
 * Provides access to gather information and expose it to telemetry representation tool.
 */
@ApplicationScoped
public class TelemetryService {

    // TODO: try to send prometheus metrics via socket to encapsulate internals.
    // 1. cannot be accessed publicly.
    @Inject
    MeterRegistry registry;

    /**
     *
     */
    @PostConstruct
    public void configure() {
//        new Socket()
    }

//
//
//
//    private Socket clientSocket;
//    private PrintWriter out;
//    private BufferedReader in;
//
//    public void startConnection(String ip, int port) {
//        clientSocket = new Socket(ip, port);
//        out = new PrintWriter(clientSocket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//    }

}
