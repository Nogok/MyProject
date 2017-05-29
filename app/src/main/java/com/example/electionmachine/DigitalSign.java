package com.example.electionmachine;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.Gson;

public class DigitalSign {

    /**
     * Тут вообще всё жутко, класс создавался для тестирования.
     * TODO Переделать!
     * */

//class Initiative{
//
//	public String description;
//	public String[] variants;
//
//	public Initiative(){
//
//	}
//	public Initiative(String description,String[] variants){
//		this.description=description;this.variants=variants;
//	}
//}
//
//class Vote{
//
//	public Initiative initiative;
//	public int variant;
//	public String publicKey;
//	public String dsaSign;
//
//	public Vote(){
//
//	}
//	public Vote(Initiative initiative,int variant,String publicKey){
//		this.initiative=initiative;
//		this.variant=variant;
//		this.publicKey=publicKey;
//	}
//}
static class DSA {
	
	public static PublicKey convertKey(byte[] data) throws InvalidKeySpecException, NoSuchAlgorithmException{
		return KeyFactory.getInstance("DSA").generatePublic(new X509EncodedKeySpec(data));
	}
  
  public static byte[] signData(byte[] data, PrivateKey key) throws Exception {
    Signature signer = Signature.getInstance("SHA1withDSA");
    signer.initSign(key);
    signer.update(data);
    return (signer.sign());
  }
  

  public static boolean verifySig(byte[] data, PublicKey key, byte[] sig) throws Exception {
    Signature signer = Signature.getInstance("SHA1withDSA");
    signer.initVerify(key);
    signer.update(data);
    return (signer.verify(sig));

  }

  public static KeyPair generateKeyPair(long seed) throws Exception {
    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA");
    SecureRandom rng = SecureRandom.getInstance("SHA1PRNG");
    rng.setSeed(seed);
    keyGenerator.initialize(1024, rng);
    return (keyGenerator.generateKeyPair());
  }
}

//public static void main(String[] args) {
//		try {
//			// ------------------
//			// на стороне клиента
//			// ------------------
//			DigitalSign ds=new DigitalSign();
//			KeyPair kp = DSA.generateKeyPair((long) 17);
//			// с сервера получаем список инициатив и выбираем интересующую. Здесь для примера я создаю новую.
//			Initiative ini = ds.new Initiative("Покрасить в зелёный", new String[]{"да","нет"});
//			// see https://stackoverflow.com/questions/5355466/converting-secret-key-into-a-string-and-vice-versa
//			byte[] buff=kp.getPublic().getEncoded();
//			Vote vote=ds.new Vote(ini,1,Base64.getEncoder().encodeToString(buff));
////			System.out.println(Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()));
//			Gson gson = new Gson();
//			System.out.println("Анкетка голоса заполнена, но не подписана");
//			String s = gson.toJson(vote);
//			System.out.println(s);
//			buff=DSA.signData(s.getBytes(), kp.getPrivate());
//			vote.dsaSign=Base64.getEncoder().encodeToString(buff);
//			System.out.println("Подписанная анкетка голоса");
//			s = gson.toJson(vote);
//			System.out.println(s);
//			// закидываем голос на сервер
//			// ------------------
//			// на стороне сервера
//			// ------------------
//			// список нераспределенных в блоки голосов.
//			LinkedList<Vote> votes = new LinkedList<Vote>();
//			// сначала проверим валидность подписи
//			String tmpDsaSign=vote.dsaSign;
//			byte[] sign=Base64.getDecoder().decode(vote.dsaSign);
//			byte[] pubKey=Base64.getDecoder().decode(vote.publicKey);
//			vote.dsaSign=null;
//			boolean valid=DSA.verifySig(gson.toJson(vote).getBytes(), DSA.convertKey(pubKey) , sign);
//			System.out.println(valid);
//			// если валидная подпись - добавляем в нераспределнные голоса.
//			if(valid)
//				{
//				vote.dsaSign=tmpDsaSign;
//				votes.add(vote);
//				};
//			// далее распределение голосов в создаваемые блоки.
//			// Подсчет голосов
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//}
}
