package org.jcrypto.operation;

import org.jcrypto.annotation.Op;
import org.jcrypto.pki.KeyPairCreator;
import org.jcrypto.util.JCryptoUtil;

import java.security.KeyPair;
import java.security.SecureRandom;

@Op(id="pki.CREATE_KEY_PAIR")
public class CreateKeyPair implements Operation {

	private int keySize;
	private String algorithm;
	private String provider;
	private SecureRandom secureRandom;
	private String sourceDir;
	private String publicKeyFileName;
	private String privateKeyFileName;
	private JCryptoUtil.KeyFormat publicStorageType;
	private JCryptoUtil.KeyFormat privateStorageType;
	private final KeyPairCreator.Builder builder;

	public CreateKeyPair() {
		builder = new KeyPairCreator.Builder();
	}

	;

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
	public Object process() throws Exception {
		KeyPairCreator keyPairCreator = builder.build();
		KeyPair keyPair = keyPairCreator.create();

		if (publicStorageType == JCryptoUtil.KeyFormat.PEM)
			JCryptoUtil.storePEMKey(this.sourceDir, this.publicKeyFileName, keyPair.getPublic().getEncoded());
		else
			JCryptoUtil.storeDERKey(this.sourceDir, this.publicKeyFileName, keyPair.getPublic().getEncoded());

		if (privateStorageType == JCryptoUtil.KeyFormat.PEM)
			JCryptoUtil.storePEMKey(this.sourceDir, this.privateKeyFileName, keyPair.getPrivate().getEncoded());
		else
			JCryptoUtil.storeDERKey(this.sourceDir, this.privateKeyFileName, keyPair.getPrivate().getEncoded());

		return "";
	}
}
