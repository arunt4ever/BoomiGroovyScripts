import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.IOException;

public class MyFTPSClient {
    public static void main(String[] args) {
        FTPSClient ftp = new FTPSClient();
        boolean error = false;
        try {
            int reply;
            String server = "ftp.server.com";
            ftp.connect(server);
            System.out.println("Connected to " + server + ".");
            System.out.print(ftp.getReplyString());

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftp.getReplyCode();

            if(!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
            }
            //FTPS Configs
            ftp.execPBSZ(0);
            ftp.execPROT("P");
            ftp.enterLocalPassiveMode();
            // Verbose Mode
            ftp.addProtocolCommandListener(new PrintCommandListener(System.out));

            ftp.login("uname","password");

            String rootDir = ftp.printWorkingDirectory();
            String currentDir = rootDir;

            String customerDirName = "Customer1";
            String customerDirPath = currentDir+"/"+customerDirName;

            // Check for the Customer folder and create if its not already there.
            if(!ftp.changeWorkingDirectory(customerDirPath)){
                ftp.makeDirectory(customerDirName);
                ftp.changeWorkingDirectory(customerDirPath);
            }

            ftp.logout();
        } catch(IOException e) {
            error = true;
            e.printStackTrace();
        } finally {
            if(ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch(IOException ioe) {
                    // do nothing
                }
            }
        }
    }
}
