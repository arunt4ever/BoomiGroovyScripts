import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NonceExampleJava {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String FOURKITES_HOST = "https://tracking-api.fourkites.com";
    private static final String FOURKITES_CLIENT_ID = "<FOURKITES_CLIENT_ID>";
    private static final String FOURKITES_API_SECRET = "<FOURKITES_API_SECRET>";

    public static void main(String [] args) {

        try {
            String path = "/api/v1/tracking?shipper=xyzshipper&load_ids=12345&pickup_start_date=2015-09-01&client_id=<client_id>&timestamp=20150929095116";
            String signature = calculateSignature("/api/v1/tracking?shipper=xyzshipper&load_ids=12345&pickup_start_date=2015-09-01&client_id=<client_id>&timestamp=20150929095116");
            System.out.println("Signature: " + signature);
            String url = FOURKITES_HOST + path + "&signature=" + signature;
            System.out.println("URL: " + url);
        } catch (SignatureException ex) {
            Logger.getLogger(NonceExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(NonceExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(NonceExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String calculateSignature(String path) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        return urlSafeEncodeBase64(calculateRFC2104HMAC(path, FOURKITES_API_SECRET));
    }

    private static byte[] calculateRFC2104HMAC(String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        urlSafeEncodeBase64("Hello".getBytes());
        return mac.doFinal(data.getBytes());
    }

    private static String urlSafeEncodeBase64(byte[] input) {
        return new String(Base64.encodeBase64(input)).replace('+','-').replace('/','_');
    }
}
