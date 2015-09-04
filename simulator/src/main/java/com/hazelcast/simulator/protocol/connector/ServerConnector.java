package com.hazelcast.simulator.protocol.connector;

import com.hazelcast.simulator.protocol.configuration.ServerConfiguration;
import com.hazelcast.simulator.protocol.core.Response;
import com.hazelcast.simulator.protocol.core.SimulatorAddress;
import com.hazelcast.simulator.protocol.core.SimulatorMessage;
import com.hazelcast.simulator.protocol.operation.SimulatorOperation;

/**
 * Connector which listens for incoming Simulator component connections.
 */
public interface ServerConnector {

    /**
     * Starts to listen on the incoming port.
     */
    void start();

    /**
     * Stops to listen on the incoming port.
     */
    void shutdown();

    /**
     * Returns the {@link SimulatorAddress} of this Simulator component.
     *
     * @return the local {@link SimulatorAddress}
     */
    SimulatorAddress getAddress();

    /**
     * Returns the {@link ServerConfiguration} of this connector.
     *
     * @return the {@link ServerConfiguration}
     */
    ServerConfiguration getConfiguration();

    /**
     * Submits a {@link SimulatorOperation} to a {@link SimulatorAddress}.
     *
     * The {@link SimulatorOperation} is put on a queue. The {@link Response} is not returned.
     *
     * @param operation the {@link SimulatorOperation} to send.
     */
    void submit(SimulatorAddress destination, SimulatorOperation operation);

    /**
     * Writes a {@link SimulatorMessage} to the {@link ClientConnector} of connected Simulator components.
     *
     * Blocks until the {@link Response} is received.
     *
     * @param destination the {@link SimulatorAddress} of the destination
     * @param operation   the {@link SimulatorOperation} to send
     * @return a {@link Response} with the result of the call
     * @throws Exception if the {@link SimulatorMessage} could not be send
     */
    Response write(SimulatorAddress destination, SimulatorOperation operation) throws Exception;

    /**
     * Writes a {@link SimulatorMessage} to the {@link ClientConnector} of connected Simulator components.
     *
     * Blocks until the {@link Response} is received.
     *
     * @param source      the {@link SimulatorAddress} of the source
     * @param destination the {@link SimulatorAddress} of the destination
     * @param operation   the {@link SimulatorOperation} to send
     * @return a {@link Response} with the result of the call
     * @throws Exception if the {@link SimulatorMessage} could not be send
     */
    Response write(SimulatorAddress source, SimulatorAddress destination, SimulatorOperation operation) throws Exception;
}