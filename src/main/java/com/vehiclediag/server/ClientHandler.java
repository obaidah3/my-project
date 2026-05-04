package com.vehiclediag.server;

import com.vehiclediag.analyzer.DiagnosticService;
import com.vehiclediag.model.DiagnosticReport;
import com.vehiclediag.model.DiagnosticRequest;
import com.vehiclediag.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Handles a single client connection to the diagnostic server.
 * Receives diagnostic requests, processes them, and sends back diagnostic reports.
 * Runs in a separate thread from the server's thread pool.
 */
public class ClientHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Constants.LOGGER_SERVER);

    private final Socket clientSocket;
    private final String clientAddress;

    /**
     * Creates a new ClientHandler for the specified client socket.
     *
     * @param clientSocket the socket connected to the client
     */
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.clientAddress = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
    }

    /**
     * Processes a single diagnostic request from the client.
     * This method is called when the handler is executed by the thread pool.
     */
    @Override
    public void run() {
        try {
            processDiagnosticRequest();
        } finally {
            closeClientConnection();
        }
    }

    /**
     * Main diagnostic processing logic.
     * Receives request, processes it, saves report, and sends response.
     */
    private void processDiagnosticRequest() {
        try {
            // Create output stream first (must be before input stream for proper serialization)
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.flush();

            // Create input stream to receive diagnostic request
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

            // Receive diagnostic request from client
            Object receivedObject = inputStream.readObject();
            if (!(receivedObject instanceof DiagnosticRequest)) {
                logger.warn("Client {} sent invalid object type: {}", clientAddress, receivedObject.getClass().getName());
                sendErrorResponse(outputStream, "Invalid request type");
                return;
            }

            DiagnosticRequest request = (DiagnosticRequest) receivedObject;
            logger.info("Client {} submitted diagnostic request for vehicle {}", 
                    clientAddress, request.getVehicle().getVehicleId());

            // Validate the diagnostic request
            request.validate();

            // Process the diagnostic request using the service
            DiagnosticService diagnosticService = new DiagnosticService();
            DiagnosticReport report = diagnosticService.processDiagnosticRequest(request);

            // Store the report persistently
            ReportStorage reportStorage = new ReportStorage();
            reportStorage.storeReport(report);
            logger.info("Report stored. Total records: {}", reportStorage.getRecordCount());

            // Send the diagnostic report back to the client
            outputStream.writeObject(report);
            outputStream.flush();
            logger.info("Diagnostic report sent to client {} for vehicle {}", 
                    clientAddress, request.getVehicle().getVehicleId());

        } catch (SocketTimeoutException e) {
            logger.warn("Client {} connection timeout: {}", clientAddress, e.getMessage());
            sendErrorResponse(e, "Connection timeout");

        } catch (EOFException e) {
            logger.info("Client {} disconnected unexpectedly (EOF)", clientAddress);

        } catch (SocketException e) {
            logger.warn("Client {} socket error: {}", clientAddress, e.getMessage());

        } catch (InvalidClassException e) {
            logger.error("Client {} serialization error (invalid class): {}", clientAddress, e.getMessage());
            sendErrorResponse(e, "Serialization error: incompatible class format");

        } catch (ClassNotFoundException e) {
            logger.error("Client {} serialization error (class not found): {}", clientAddress, e.getMessage());
            sendErrorResponse(e, "Serialization error: class not found");

        } catch (IOException e) {
            logger.error("Client {} I/O error: {}", clientAddress, e.getMessage());

        } catch (IllegalArgumentException e) {
            logger.error("Client {} submitted invalid diagnostic data: {}", clientAddress, e.getMessage());
            sendErrorResponse(e, "Invalid diagnostic data: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error processing client {}: {}", clientAddress, e.getMessage(), e);
            sendErrorResponse(e, "Unexpected server error: " + e.getMessage());
        }
    }

    /**
     * Sends an error response to the client.
     * Attempts to send error message via ObjectOutputStream.
     *
     * @param exception the exception that occurred
     * @param message the error message to send
     */
    private void sendErrorResponse(Exception exception, String message) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject("ERROR: " + message);
            outputStream.flush();
            logger.info("Error response sent to client {}: {}", clientAddress, message);
        } catch (IOException e) {
            logger.error("Failed to send error response to client {}: {}", clientAddress, e.getMessage());
        }
    }

    /**
     * Sends an error response to the client.
     * Overloaded version that creates ObjectOutputStream internally.
     *
     * @param outputStream the output stream to write the error to
     * @param message the error message to send
     */
    private void sendErrorResponse(ObjectOutputStream outputStream, String message) {
        try {
            outputStream.writeObject("ERROR: " + message);
            outputStream.flush();
            logger.info("Error response sent to client {}: {}", clientAddress, message);
        } catch (IOException e) {
            logger.error("Failed to send error response to client {}: {}", clientAddress, e.getMessage());
        }
    }

    /**
     * Closes the client socket connection.
     * Ensures all resources are released.
     */
    private void closeClientConnection() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
                logger.info("Client {} connection closed", clientAddress);
            }
        } catch (IOException e) {
            logger.error("Error closing client {} socket: {}", clientAddress, e.getMessage());
        }
    }
}