package org.jcrypto.operation;

import com.google.common.collect.ImmutableMap;
import org.jcrypto.annotation.Op;
import org.jcrypto.model.Response;
import org.jcrypto.pki.KeyPairCreator;
import org.jcrypto.util.JCryptoUtil;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

@Op(id="pki.CREATE_KEY_PAIR")
public class CreateKeyPair implements Operation {

	private int keySize;
	private String algorithm;
	private String provider;
	private SecureRandom secureRandom;
	private boolean store;
	private String sourceDir;
	private String publicKeyFileName;
	private String privateKeyFileName;
	private JCryptoUtil.KeyFormat publicStorageType;
	private JCryptoUtil.KeyFormat privateStorageType;
	private final KeyPairCreator.Builder builder;

	public CreateKeyPair() {
		builder = new KeyPairCreator.Builder();
	}

	public void setKeySize(int keySize) {
		builder.withKeySize(keySize);
	}

	public void setAlgorithm(String algorithm) {
		builder.withAlgorithm(algorithm);
	}

	public void setProvider(String provider) {
		builder.withProvider(provider);
	}

	public void setSecureRandom(SecureRandom secureRandom) {
		builder.withSecureRandom(secureRandom);
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public void setPublicKeyFileName(String publicKeyFileName) {
		this.publicKeyFileName = publicKeyFileName;
	}

	public void setPrivateKeyFileName(String privateKeyFileName) {
		this.privateKeyFileName = privateKeyFileName;
	}

	public void setPublicStorageType(String publicStorageType) {
		this.publicStorageType = JCryptoUtil.KeyFormat.valueOf(publicStorageType);
	}

	public void setPrivateStorageType(String privateStorageType) {
		this.privateStorageType = JCryptoUtil.KeyFormat.valueOf(privateStorageType);
	}

	@Override
	public Response process() {
		KeyPairCreator keyPairCreator = builder.build();
		KeyPair keyPair = null;
		try {
			keyPair = keyPairCreator.create();
		} catch (NoSuchAlgorithmException e) {
			return Response.errorResponse(e);
		} catch (NoSuchProviderException e) {
			return Response.errorResponse(e);
		}

		if (!store)
			return Response.successResponse(ImmutableMap.of(
					"private_key", JCryptoUtil.makePrivatePEM(keyPair.getPrivate().getEncoded()),
					"public_key", JCryptoUtil.makePublicPEM(keyPair.getPublic().getEncoded())));
		try {
			byte[] content = keyPair.getPublic().getEncoded();
			JCryptoUtil.storePublicKey(publicStorageType, sourceDir, publicKeyFileName, content);

			content = keyPair.getPrivate().getEncoded();
			JCryptoUtil.storePrivateKey(privateStorageType, sourceDir, privateKeyFileName, content);
		} catch (IOException e) {
			return Response.errorResponse(e);
		}
		return Response.successResponse(ImmutableMap.of());
	}
}
