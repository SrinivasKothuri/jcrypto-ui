package org.jcrypto.server;

import org.junit.Test;

public class BootstrapTest {

    @Test
    public void textServerStart() throws Exception {
        Bootstrap.INSTANCE.start();
    }
}
