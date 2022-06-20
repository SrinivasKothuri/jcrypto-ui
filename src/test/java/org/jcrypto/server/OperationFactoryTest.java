package org.jcrypto.server;

import com.google.common.collect.ImmutableMap;
import org.jcrypto.operation.OperationFactory;
import org.junit.Test;

public class OperationFactoryTest {

	@Test
	public void testKeyPairOperation() throws Exception {
		OperationFactory.prepareOperation("pki.CREATE_KEY_PAIR",
				ImmutableMap.of("key_size", "2048", "algorithm", "RSA",
						"src_dir", "/Users/srinivas.kothuri",
						"public_key_file_name", "pub.key",
						"private_key_file_name", "priv.key",
						"public_key_storage_type", "PEM",
						"private_key_storage_type", "PEM")).process();
	}
}
