package org.jcrypto.server;

public enum Bootstrap {

    INSTANCE;

    private Server server = new Server();

    public void start() throws Exception {
        server.setPort(4270);
        server.start();
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static void main(String[] args) throws Exception {
        INSTANCE.start();
    }
}
