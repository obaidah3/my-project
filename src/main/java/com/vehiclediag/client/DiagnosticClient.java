package com.vehiclediag.client;

import com.vehiclediag.model.DiagnosticReport;
import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Client for connecting to the diagnostic server.
 * Handles socket communication and serialization of diagnostic requests/reports.
 */
public class DiagnosticClient {

    private static final Logger logger = LoggerFactory.getLogger(Constants.LOGGER_CLIENT);

    private final String serverHost;
    private final int serverPort;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    /**
     * Creates a new diagnostic client for the specified server.
     *
     * @param serverHost the server hostname or IP address
     * @param serverPort the server port
     */
    public DiagnosticClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    /**
     * Establishes a connection to the diagnostic server.
     *
     * @throws IOException if connection fails
     */
    public void connect() throws IOException {
        try {
            socket = new Socket(serverHost, serverPort);
            socket.setSoTimeout(Constants.SOCKET_TIMEOUT_MS);

            // Create output stream first (must be before input stream)
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();

            // Create input stream to receive responses
            inputStream = new ObjectInputStream(socket.getInputStream());

            logger.info("Connected to diagnostic server at {}:{}", serverHost, serverPort);
        } catch (ConnectException e) {
            logger.error("Failed to connect to server at {}:{}: {}", serverHost, serverPort, e.getMessage());
            throw new IOException("Connection refused. Server may not be running.", e);
        } catch (IOException e) {
            logger.error("I/O error connecting to server: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Sends a diagnostic request to the server and receives the diagnostic report.
     *
     * @param request the diagnostic request to send
     * @return the diagnostic report from the server
     * @throws IOException if communication fails
     * @throws ClassNotFoundException if response object class is not found
     * @throws IllegalStateException if not connected to server
     */
    public Object sendRequest(DiagnosticRequest request) throws IOException, ClassNotFoundException {
        if (socket == null || socket.isClosed()) {
            throw new IllegalStateException("Not connected to server. Call connect() first.");
        }

        if (request == null) {
            throw new IllegalArgumentException("DiagnosticRequest cannot be null");
        }

        try {
            // Send request to server
            outputStream.writeObject(request);
            outputStream.flush();
            logger.info("Diagnostic request sent for vehicle {}", request.getVehicle().getVehicleId());

            // Receive response from server
            Object response = inputStream.readObject();
            logger.info("Response received from server");
            return response;

        } catch (EOFException e) {
            logger.error("Server disconnected unexpectedly: {}", e.getMessage());
            throw new IOException("Server closed connection unexpectedly", e);
        } catch (SocketException e) {
            logger.error("Socket error during communication: {}", e.getMessage());
            throw new IOException("Network communication error", e);
        } catch (IOException e) {
            logger.error("I/O error during request/response: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Closes the connection to the server.
     */
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                logger.info("Disconnected from diagnostic server");
            }
        } catch (IOException e) {
            logger.error("Error closing socket: {}", e.getMessage());
        }
    }

    /**
     * Returns whether the client is currently connected to the server.
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return socket != null && !socket.isClosed();
    }

    /**
     * Returns the server host.
     *
     * @return the server hostname
     */
    public String getServerHost() {
        return serverHost;
    }

    /**
     * Returns the server port.
     *
     * @return the server port
     */
    public int getServerPort() {
        return serverPort;
    }
}