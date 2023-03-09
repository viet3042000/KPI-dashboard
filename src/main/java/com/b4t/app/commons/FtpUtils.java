package com.b4t.app.commons;

/**
 * @author tamdx
 */

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class FtpUtils {

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.path}")
    private String path;

    @Value("${ftp.port}")
    private int port;

    private static final String ERROR_CONNECT = "Exception in connecting to FTP Server";

    public FtpUtils() {
    }

    private static final Logger log = LoggerFactory.getLogger(FtpUtils.class);

    public FTPFile[] listFiles() throws Exception {
        FTPFile[] files = null;
        String pathFtp = this.path + "/" + DateUtil.dateToString(new Date(), "yyyy/MM/dd") + "/";
        FTPClient ftpClient = connect();
        if (ftpClient != null) {
            createDirectory(ftpClient, pathFtp);
            files = ftpClient.listFiles();
            try {
                close(ftpClient);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return files;
    }

    public void uploadFile(String fileName, InputStream inputStream) throws Exception {
        String path = this.path + "/" + DateUtil.dateToString(new Date(), "yyyy/MM/dd") + "/";
        log.info("Upload file " + fileName + " len duong dan:" + path);
        uploadFile(this.host, this.username, this.password, this.port, path, fileName, inputStream);
    }

    public void uploadFile(String fileName, String path, InputStream inputStream) throws Exception {
        log.info("Upload file " + fileName + " len duong dan:" + path);
        uploadFile(this.host, this.username, this.password, this.port, path, fileName, inputStream);
    }
//    public void uploadFile(File fileUpload) throws Exception {
//        String path = this.path + "/" + DateUtil.dateToString(new Date(), "yyyy/MM/dd") + "/";
//        log.info("Upload file " + fileUpload.getName() + " len duong dan:" + path);
//        uploadFile(this.host, this.username, this.password, this.port, path, fileUpload);
//    }

    private FTPClient connect() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(this.host, this.port);
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new Exception(ERROR_CONNECT);
        }
        boolean isLogin = ftpClient.login(username, password);
        if (isLogin) {
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient;
        } else {
            return null;
        }
    }

    private void close(FTPClient ftpClient) throws Exception {
        if (ftpClient.isConnected()) {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    public void uploadFile(String host, String username, String password, int port, String pathRemote, String fileName, InputStream inputStream) throws Exception {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new Exception(ERROR_CONNECT);
            }
            boolean isLogin = ftpClient.login(username, password);
            if (isLogin) {
                ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                createDirectory(ftpClient, pathRemote);
                log.info("Bat dau upload file");
                boolean result = ftpClient.storeFile(fileName, inputStream);
                System.out.println("Ket qua upload file " + fileName + ":" + result);

            } else {
                log.error("Login FTP host:" + host + " port:"+ port + " user:" + username + " path:" + pathRemote + " fail! ");
            }
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }

//    public void copyFileFtp(String sourcePath, String targetPath) throws Exception {
//        copyFileFtp(host, username, password, port, sourcePath, targetPath);
//    }
//
//    private void copyFileFtp(String host, String username, String password, int port, String sourcePath, String targetPath) throws Exception {
//        FTPClient ftpClient = new FTPClient();
//        try {
//            ftpClient.connect(host, port);
//            int reply = ftpClient.getReplyCode();
//            if (!FTPReply.isPositiveCompletion(reply)) {
//                ftpClient.disconnect();
//                throw new Exception(ERROR_CONNECT);
//            }
//            boolean isLogin = ftpClient.login(username, password);
//            if (isLogin) {
//                ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
//                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//                ftpClient.enterLocalPassiveMode();
//
//                ftpClient.sendCommand(String.format("SITE EXEC cp -p %s %s", sourcePath, targetPath));
//            }
//        } finally {
//            if (ftpClient.isConnected()) {
//                ftpClient.logout();
//                ftpClient.disconnect();
//            }
//        }
//    }


//    public void uploadFile(String host, String username, String password, int port, String pathRemote, File fileUpload) throws Exception {
//        FTPClient ftpClient = new FTPClient();
//        try {
//            ftpClient.connect(host, port);
//            int reply = ftpClient.getReplyCode();
//            if (!FTPReply.isPositiveCompletion(reply)) {
//                ftpClient.disconnect();
//                throw new Exception(ERROR_CONNECT);
//            }
//            boolean isLogin = ftpClient.login(username, password);
//            if (isLogin) {
//                ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
//                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//                ftpClient.enterLocalPassiveMode();
//                createDirectory(ftpClient, pathRemote);
//                try(InputStream inputStream = new FileInputStream(fileUpload)) {
//                    boolean result = ftpClient.storeFile(fileUpload.getName(), inputStream);
//                    System.out.println("Ket qua upload file " + fileUpload.getName() + ":" + result);
//                }
//            }
//        } finally {
//            if (ftpClient.isConnected()) {
//                ftpClient.logout();
//                ftpClient.disconnect();
//            }
//        }
//    }

    //    public void uploadFile(String host, String username, String password, int port, String pathRemote, List<MultipartFile> lstMultipartFile, String preName) throws Exception {
//        FTPClient ftpClient = new FTPClient();
//        try {
//            ftpClient.connect(host, port);
//            int reply = ftpClient.getReplyCode();
//            if (!FTPReply.isPositiveCompletion(reply)) {
//                ftpClient.disconnect();
//                throw new Exception(ERROR_CONNECT);
//            }
//            boolean isLogin = ftpClient.login(username, password);
//            if (isLogin) {
//                ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
//                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//                ftpClient.enterLocalPassiveMode();
//                createDirectory(ftpClient, pathRemote);
//                for (MultipartFile multipartFile : lstMultipartFile) {
//                    boolean result = ftpClient.storeFile(preName + DataUtil.getSafeFileName(multipartFile.getOriginalFilename()), multipartFile.getInputStream());
//                    System.out.println("Ket qua upload file " + DataUtil.getSafeFileName(multipartFile.getOriginalFilename()) + ":" + result);
//                }
//            }
//        } finally {
//            this.releaseConnection(ftpClient);
//        }
//    }
    private void releaseConnection(FTPClient ftpClient) throws Exception {
        if (ftpClient.isConnected()) {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    private void createDirectory(FTPClient ftpClient, String dir) throws Exception {
        boolean dirExists = true;
        String[] directories = dir.split("/");
        for (String directory : directories) {
            if (!directory.isEmpty()) {
                dirExists = ftpClient.changeWorkingDirectory(directory);
            }
            if (!dirExists) {
                ftpClient.makeDirectory(directory);
                ftpClient.changeWorkingDirectory(directory);
            }
        }
    }

    public byte[] downloadFile(String remoteFileName) throws Exception {
        String path = this.path + "/" + DateUtil.dateToString(new Date(), new SimpleDateFormat("yyyy/MM/dd")) + "/";
        return downloadFile(host, username, password, port, path, remoteFileName);
    }

    public byte[] downloadFile(String remoteFolder, String remoteFileName) throws Exception {
        return downloadFile(host, username, password, port, remoteFolder, remoteFileName);
    }

//    public byte[] downloadFile(String host, String username, String password, int port,
//                               String pathRemote, String remoteFileName) throws Exception {
//        return downloadFile(host, username, password, port, pathRemote, remoteFileName);
//    }

    public InputStream retrieveFileFromFTPServer(String folderRemote, String fileName) throws Exception {
        return retrieveFileFromFTPServer(host, username, password, port, folderRemote, fileName);
    }

    private InputStream retrieveFileFromFTPServer(String host, String username, String password, int port,
                                                  String folderRemote, String fileName) throws Exception {
        FTPClient ftpClient1 = new FTPClient();
        InputStream inputStream1 = null;
        try {
            ftpClient1.connect(host, port);
            int reply = ftpClient1.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient1.disconnect();
                throw new Exception(ERROR_CONNECT);
            }
            boolean isLogin = ftpClient1.login(username, password);
            if (isLogin) {
                log.info("login ftp success!!!");
                ftpClient1.enterLocalPassiveMode();
                ftpClient1.setFileType(FTP.BINARY_FILE_TYPE);

                String[] directories = folderRemote.split("/");
                for (String directory : directories) {
                    if (!directory.isEmpty()) {
                        ftpClient1.changeWorkingDirectory(directory);
                    }
                }
            }
            inputStream1 = ftpClient1.retrieveFileStream(folderRemote + fileName);

        } finally {
            if (ftpClient1.isConnected()) {
//                ftpClient.logout();
                ftpClient1.disconnect();
            }
        }
        return inputStream1;
    }


    public File retrieveFileFromFTPServer(String host, String username, String password, String fileName, String destinationDir, String tempDir) throws Exception {
        FTPClient client = new FTPClient();
        boolean result = false;
        try {
            // connect, login, set timeout
            client.connect(host);
            client.login(username, password);

            // set transfer type
            client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();

            // set timeout
            client.setSoTimeout(60000); // default 1 minute

            // check connection
            int reply = client.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                // switch to working folder
                client.changeWorkingDirectory(destinationDir);
                // create temp file on temp directory
                fileName = StringUtils.getSafeFileName(fileName);
                File tempFile = new File(tempDir + File.separator + fileName);
//                List<String> lstData = new ArrayList<String>();

                // retrieve file from server and logout
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
                if (client.retrieveFile(destinationDir + File.separator + fileName, outputStream)) {
                    outputStream.close();
                }
                // close connection
                client.logout();
//                if (tempFile.isFile()) {
//                    InputStream inputStream = new FileInputStream(tempFile);
//                    BufferedInputStream bInf = new BufferedInputStream(inputStream);
//                    byte[] buffer = new byte[(int) tempFile.length()];
//                    bInf.read(buffer);
//                    byte[] encode = Base64.encodeBase64(buffer);
//                    String strData = new String(encode, Charset.forName("UTF-8"));
//                    lstData.add(strData);
//                    inputStream.close();
//                }
                //delete file
//                tempFile.delete();

                // return file
                result = true;
                return tempFile;
            } else {
                client.disconnect();
                System.err.println("FTP server refused connection !");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            client.disconnect();
        }
        return null;
    }

    public byte[] downloadFile(String host, String username, String password, int port,
                               String pathRemote, String remoteFileName) throws Exception {
        FTPClient ftpClient = new FTPClient();
        byte[] byteArray = null;
        try {
            ftpClient.connect(host, port);
            ftpClient.setSoTimeout(60000); // default 1 minute
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new Exception(ERROR_CONNECT);
            }
            boolean isLogin = ftpClient.login(username, password);
            if (isLogin) {
                log.info("login ftp success!!!");
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


                String[] directories = pathRemote.split("/");
                for (String directory : directories) {
                    if (!directory.isEmpty()) {
                        ftpClient.changeWorkingDirectory(directory);
                    }
                }

                try (ByteArrayOutputStream is = new ByteArrayOutputStream()) {
                    log.info("downloading file:" + pathRemote + remoteFileName);
                    boolean result = ftpClient.retrieveFile(remoteFileName, is);
                    byteArray = is.toByteArray();
                    if (!result) {
                        log.error(String.format("Can't download file %s/%s: %s (%s)",
                                pathRemote, remoteFileName, ftpClient.getReplyString(), ftpClient.getReplyCode()));
                    }
                }
            }
            return byteArray;
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    public void createFolder(String pathFile) {
        String server = "103.16.1.59";
        int port = 21;
        String user = "ftp_app";
        String pazz = "Mic#2020";
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            String remotePath = "/remote/path/file.txt";
            FTPFile remoteFile = ftpClient.mlistFile(remotePath);
            if (remoteFile != null) {
                System.out.println("File " + remoteFile.getName() + " exists");
            } else {
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    System.out.println("Operation failed. Server reply code: " + replyCode);
                    return;
                }
                boolean success = ftpClient.login(user, pazz);
                showServerReply(ftpClient);
                if (!success) {
                    System.out.println("Could not login to the server");
                    return;
                }
                // Creates a directory
                String dirToCreate = pathFile;
                success = ftpClient.makeDirectory(dirToCreate);
                showServerReply(ftpClient);
                if (success) {
                    System.out.println("Successfully created directory: " + dirToCreate);
                } else {
                    System.out.println("Failed to create directory. See server's reply.");
                }
                // logs out
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            log.error(ex.getMessage(), ex);
        }
    }

}

