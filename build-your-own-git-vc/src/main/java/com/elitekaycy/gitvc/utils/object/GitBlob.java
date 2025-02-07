package com.elitekaycy.gitvc.utils.object;

import java.security.MessageDigest;

public class GitBlob {

    private final String repoDir = ".vcs";
    private final byte[] content;
    private final String hash;

    public GitBlob(String filePath) {
        try { 
            byte[] content = Files.readAllBytes(Paths.get(filePath));
            this(createBlob(content), content);
        }
        catch(IOException ex) {
            ex.printStrackTrace();
        }
    }

    public GitBlob(String hash, byte[] content) {
        this.content = content;
        this.hash = hash;
    }

    public byte[] getContent() {
        return this.content;
    }

    public String getHash() {
        return this.hash;
    }

    public String createBlob(byte[] content) {
        String hash = sha1(content);
        return hash;
    }

    private String sha1(byte[] content) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return byteToHex(md.digest(content));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        ;
    }

    private String byteToHex(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
