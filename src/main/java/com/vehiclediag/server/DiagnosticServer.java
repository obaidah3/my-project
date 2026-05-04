package com.vehiclediag.server;

import com.vehiclediag.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Multi-threaded diagnostic server that accepts client connections.
 * Each client connection is handled by a dedicated thread from a thread pool.
 * Provides thread-safe, concurrent diagnostic processing for multiple vehicles.
 */
public class DiagnosticServer {

    private static final Logger logger = LoggerFactory.getLogger(Constants.LOGGER_SERVER);
    
    private final int port;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private volatile boolean running = false;

    /**
     * Creates a new DiagnosticServer that listens on the default port.
     */
    public DiagnosticServer() {
        this(Constants.SERVER_PORT);
    }

    /**
     * Creates a new DiagnosticServer that listens on the specified port.
     *
     * @param port the port to listen on
     */
    public DiagnosticServer(int port) {
        this.port = port;
    }

    /**
     * Starts the diagnostic server and begins accepting client connections.
     * Blocks until the server is shut down.
     *
     * @throws IOException if the server socket cannot be created
     */
    public void start() throws IOException {
        if (running) {
            logger.warn("Server is already running");
            return;
        }

        // Create thread pool with configurable core and max sizes
        executorService = new ThreadPoolExecutor(
                Constants.THREAD_POOL_CORE_SIZE,
                Constants.THREAD_POOL_MAX_SIZE,
                Constants.THREAD_POOL_KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );

        serverSocket = new ServerSocket(port);
        running = true;

        logger.info("Diagnostic Server started on port {}", port);
        logger.info("Thread pool size: {} - {}", Constants.THREAD_POOL_CORE_SIZE, Constants.THREAD_POOL_MAX_SIZE);

        try {
            acceptClientConnections();
        } finally {
            stop();
        }
    }

    /**
     * Accepts incoming client connections in a loop.
     * Each client is assigned to a ClientHandler and submitted to the thread pool.
     */
    private void acceptClientConnections() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(Constants.SOCKET_TIMEOUT_MS);

                logger.info("New client connection from {}:{}", 
                        clientSocket.getInetAddress().getHostAddress(), 
                        clientSocket.getPort());

                // Create handler and submit to thread pool for concurrent processing
                ClientHandler handler = new ClientHandler(clientSocket);
                executorService.execute(handler);

            } catch (IOException e) {
                if (running) {
                    logger.error("Error accepting client connection", e);
                } else {
                    logger.info("Server socket closed");
                }
            }
        }
    }

    /**
     * Stops the diagnostic server.
     * Gracefully shuts down the thread pool and closes the server socket.
     */
    public void stop() {
        if (!running) {
            logger.warn("Server is not running");
            return;
        }

        running = false;
        logger.info("Shutting down diagnostic server...");

        // Close server socket to stop accepting new connections
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                logger.info("Server socket closed");
            }
        } catch (IOException e) {
            logger.error("Error closing server socket", e);
        }

        // Shutdown thread pool gracefully
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                // Wait for existing tasks to complete
                if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                    logger.warn("Thread pool did not terminate within timeout, forcing shutdown");
                    executorService.shutdownNow();
                }
                logger.info("Thread pool shut down successfully");
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for thread pool to shutdown", e);
                executorService.shutdownNow();
            }
        }

        logger.info("Diagnostic server stopped");
    }

    /**
     * Returns whether the server is currently running.
     *
     * @return true if the server is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Returns the port the server is listening on.
     *
     * @return the server port
     */
    public int getPort() {
        return port;
    }

    /**
     * Main entry point to start the diagnostic server.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        DiagnosticServer server = new DiagnosticServer();
        
        // Graceful shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown signal received");
            server.stop();
        }));

        try {
            server.start();
        } catch (IOException e) {
            logger.error("Failed to start diagnostic server", e);
            System.exit(1);
        }
    }
}