package main.java.io.searchbox.client.config.idle;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.nio.conn.NHttpClientConnectionManager;

import io.searchbox.client.config.idle.ReapableConnectionManager;

import java.util.concurrent.TimeUnit;

public class HttpReapableConnectionManager implements ReapableConnectionManager {
    private final HttpClientConnectionManager connectionManager;
    private final NHttpClientConnectionManager nConnectionManager;

    public HttpReapableConnectionManager(HttpClientConnectionManager connectionManager, NHttpClientConnectionManager nConnectionManager) {
        if(connectionManager == null || nConnectionManager == null) throw new IllegalArgumentException();

        this.connectionManager = connectionManager;
        this.nConnectionManager = nConnectionManager;
    }

    public void closeIdleConnections(long idleTimeout, TimeUnit unit) {
        connectionManager.closeIdleConnections(idleTimeout, unit);
        nConnectionManager.closeIdleConnections(idleTimeout, unit);
    }
}
