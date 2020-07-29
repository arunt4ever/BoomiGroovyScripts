import com.boomi.execution.ExecutionUtil
import groovy.transform.Field

import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

logger = ExecutionUtil.getBaseLogger()

@Field
StringBuilder sb = new StringBuilder()

@Field
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH);

try {
    char[] passphrase;
    char SEP = File.separatorChar;
    File dir = new File(System.getProperty("java.home") + SEP
            + "lib" + SEP + "security");
    File file = new File(dir, "jssecacerts");
    if (file.isFile() == false) {
        file = new File(dir, "cacerts");
    }
    logger.info("Loading KeyStore " + file + "...");
    InputStream inputStream = file.newInputStream();
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(inputStream, passphrase);
    inputStream.close();
    getCertificateList(ks);
} catch (FileNotFoundException | KeyStoreException e) {
    e.printStackTrace();
} catch (CertificateException e) {
    e.printStackTrace();
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}

void getCertificateList(KeyStore ks) {
    logger.info("Listing Certificates in the KeyStore....");
    HashMap<String, Certificate> certs = new HashMap<String, Certificate>();
    try {
        Enumeration<String> aliases = ks.aliases();
        sb.append("Alias, ValidToDate, ValidFlag");
        sb.append("\n");
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Certificate cert = ks.getCertificate(alias);
            sb.append(parseCertificateInfo(alias, cert));
            sb.append("\n");
        }
    } catch (KeyStoreException e) {
        e.printStackTrace();
    }
}

String parseCertificateInfo(String alias, Certificate cert) {
    String certString = cert.toString();

    int startIndex = certString.indexOf("To:") + 3;
    String validToString = certString.substring(startIndex).trim();
    String validToDate = validToString.substring(0, validToString.indexOf("]")).trim()
    boolean validResult = false
    try {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(validToDate, formatter)
        validResult = zonedDateTime.isAfter(ZonedDateTime.now());
    }catch (DateTimeParseException e){
        logger.info(e.printStackTrace())
    }
    return alias + "," + validToDate + "," + validResult;
}


for (int i = 0; i < dataContext.getDataCount(); i++) {
    InputStream is = dataContext.getStream(i)
    Properties props = dataContext.getProperties(i)

    dataContext.storeStream(new ByteArrayInputStream(sb.toString().getBytes('UTF-8')), props)
}
