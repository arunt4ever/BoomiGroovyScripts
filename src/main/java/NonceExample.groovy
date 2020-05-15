import com.boomi.execution.ExecutionUtil
import org.apache.commons.codec.binary.Base64

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SignatureException
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Level

logger = ExecutionUtil.getBaseLogger()

String HMAC_SHA1_ALGORITHM = "HmacSHA1"
String FOURKITES_HOST = ExecutionUtil.getDynamicProcessProperty("DPP_API_HOSTNAME")
String FOURKITES_CLIENT_ID = ExecutionUtil.getDynamicProcessProperty("DPP_API_CLIENT_ID")
String FOURKITES_API_SECRET = ExecutionUtil.getDynamicProcessProperty("DPP_API_SECRET")
String relative_url = ""

try {
    String path = ExecutionUtil.getDynamicProcessProperty("DPP_RELATIVE_URL")
    ZonedDateTime zonedDateTime = Instant.now().atZone(ZoneId.of("UTC"))
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    String timeStamp = zonedDateTime.format(formatter)
    logger.info("TimeStamp in UTC: "+timeStamp)
    
    path = path + "&timestamp=" + timeStamp
    //path = path + "&timestamp=20190402144746";
    
    String signature = calculateSignature(path,FOURKITES_API_SECRET,HMAC_SHA1_ALGORITHM)
    logger.info("Signature: " + signature)
    relative_url = path + "&signature=" + signature
    logger.info("Relative URL: " + relative_url)
    String url = FOURKITES_HOST + path + "&signature=" + signature
    logger.info("URL: " + url)
    ExecutionUtil.setDynamicProcessProperty("DPP_RELATIVE_URL",relative_url,false)
} catch (SignatureException ex) {
    logger.error(NonceExample.class.getName()).log(Level.SEVERE, null, ex)
} catch (NoSuchAlgorithmException ex) {
    logger.error(NonceExample.class.getName()).log(Level.SEVERE, null, ex)
} catch (InvalidKeyException ex) {
    logger.error(NonceExample.class.getName()).log(Level.SEVERE, null, ex)
}

for( int i = 0; i < dataContext.getDataCount(); i++ ) {
    InputStream is = dataContext.getStream(i)
    Properties props = dataContext.getProperties(i)
    
    relative_url = relative_url.substring(1)
    props.setProperty("document.dynamic.userdefined.DDP_RELATIVE_URL", relative_url)
    
    dataContext.storeStream(is, props)
}

String calculateSignature(String path, String FOURKITES_API_SECRET, String HMAC_SHA1_ALGORITHM) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
    return urlSafeEncodeBase64(calculateRFC2104HMAC(path, FOURKITES_API_SECRET, HMAC_SHA1_ALGORITHM))
}

byte[] calculateRFC2104HMAC(String data, String key, String HMAC_SHA1_ALGORITHM)
        throws NoSuchAlgorithmException, InvalidKeyException {
    SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM)
    Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM)
    mac.init(signingKey)
    return mac.doFinal(data.getBytes())
}

String urlSafeEncodeBase64(byte[] input) {
    return new String(Base64.encodeBase64(input)).replace('+','-').replace('/','_')
}
