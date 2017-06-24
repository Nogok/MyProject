package com.example.electionmachine;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;



class DigitalSign {

    /**
     * Класс создания цифровой подписи пользователя
     * */

  static PrivateKey convertPrivateKey(byte[] data) throws InvalidKeySpecException, NoSuchAlgorithmException{
        KeyFactory kf = KeyFactory.getInstance("DSA");
        return kf.generatePrivate(new PKCS8EncodedKeySpec(data));
  }
  
  static byte[] signData(byte[] data, PrivateKey key) throws Exception {
    Signature signer = Signature.getInstance("SHA1withDSA");
    signer.initSign(key);
    signer.update(data);
    return (signer.sign());
  }


  static KeyPair generateKeyPair(long seed) throws Exception {
    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA");
    SecureRandom rng = SecureRandom.getInstance("SHA1PRNG");
    rng.setSeed(seed);
    keyGenerator.initialize(1024, rng);
    return (keyGenerator.generateKeyPair());
  }


}
