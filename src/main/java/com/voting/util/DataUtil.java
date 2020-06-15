package com.voting.util;

import com.voting.constants.StringConstant;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
}
