package com.voting.util;

import com.voting.constants.ErrorConstant;
import com.voting.constants.StringConstant;
import com.voting.model.response.BaseResponse;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.UUID;

public class DataUtil {
    private static final Logger logger = LogManager.getLogger(DataUtil.class);

    static final int KEY_SIZE   = 512;
    private static KeyPair keyPair;

    public static String createRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String convertPublicKeyToString(PublicKey publicKey) {
        String plk = "";
        BASE64Encoder base64Encoder = new BASE64Encoder();
        plk = base64Encoder.encode(publicKey.getEncoded()).replaceAll("\\r\\n|\\r|\\n|", "");
        return plk;
    }

    public static String convertPrivateKeyToString(PrivateKey privateKey) {
        String prkey = "";
        BASE64Encoder base64Encoder = new BASE64Encoder();
        prkey = base64Encoder.encode(privateKey.getEncoded()).replaceAll("\\r\\n|\\r|\\n", "");
        return prkey;
    }

    public static String generateWalletId(String logId) {
        return System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", "");
    }

    public static JsonObject generateKeyPair(String logId) {
        JsonObject keyObject = new JsonObject();
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(KEY_SIZE);  //512 bytes provides an acceptable security level
            keyPair = keyGen.genKeyPair();
            // Set the public and private keys from the keyPair
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            keyObject.put(StringConstant.WALLET_ADDRESS, DataUtil.convertPublicKeyToString(publicKey));
            keyObject.put(StringConstant.WALLET_PRIMARY, DataUtil.convertPrivateKeyToString(privateKey));

        } catch (Exception e) {
            logger.warn("{}| Algorithm not supported: {}", logId, e.getMessage());
        }
        return keyObject;
    }

    public static String convertTimeWithFormat(long timeInMillisecond, String format) {
        try {
            DateFormat f = new SimpleDateFormat(format);
            return f.format(timeInMillisecond);
        } catch (Exception e) {
            return "";
        }
    }

    public static BaseResponse buildResponse(int resultCode, String requestId, String responseBody) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResultCode(resultCode);
        baseResponse.setMessage(ErrorConstant.getMessage(resultCode));
        baseResponse.setResponseTime(System.currentTimeMillis());
        baseResponse.setRequestId(requestId);
        if (responseBody != null) {
            baseResponse.setData(new JsonObject(responseBody));
        }

        return baseResponse;
    }

    public static byte[] applyRSASig(String privateKey, String input) {
        PrivateKey prvk;
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
            byte[] strByte = input.getBytes();

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            prvk = keyFactory.generatePrivate(privateKeySpec);
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(prvk);
            rsa.update(strByte);
            return rsa.sign();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean verifySignatureBase64(String signature, String toBeSign, String publicKey) {
        try {
            PublicKey pubk;
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            pubk = keyFactory.generatePublic(publicKeySpec);
            Signature sign = Signature.getInstance("SHA1withRSA");
            byte[] ssignature = Base64.getDecoder().decode(signature);
            sign.initVerify(pubk);
            sign.update(toBeSign.getBytes());
            return sign.verify(ssignature);
        } catch (NoSuchAlgorithmException e) {
            //logger.info("VERIFY DATA - NoSuchAlgorithmException");
            //logger.error("NoSuchAlgorithmException, " + e.getMessage());
        } catch (InvalidKeyException e) {
            //logger.info("VERIFY DATA - InvalidKeyException");
            //logger.error("InvalidKeyException, " + e.getMessage());
        } catch (SignatureException e) {
            //logger.info("VERIFY DATA - SignatureException");
            //logger.error("SignatureException, " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            //logger.info("VERIFY DATA - InvalidKeySpecException");
            //logger.error("InvalidKeySpecException, " + e.getMessage());
        }
        return false;
    }

    public static String calculateHash(String previousHash, long currentTime, long nonce, String transId, int index) {
        return applySha256(previousHash + currentTime + nonce + transId + index);
    }

    //Encrypt SHA256
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
