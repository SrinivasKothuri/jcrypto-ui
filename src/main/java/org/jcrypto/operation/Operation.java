package org.jcrypto.operation;

import org.jcrypto.model.Response;

public interface Operation {
	Response process() throws Exception;
}
