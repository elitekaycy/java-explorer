package main.java.com.elitekaycy.gitvc.utils.methods;

public class GitStaging {

    public static final String STAGING_DIR = "./.vcs/staging";

    public GitStaging(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return;
        }

        String blobHash = GitBlob.createBlob(filePath);

        try {
            storeBlob(filePath, blob);
        } catch (IOException e) {
            System.out.println("Error storing blob: " + e.getMessage());
            return;
        }

        updateStagingArea(filePath, blobHash);
    }

    private static void storeBlob(String filePath, String hash) throws IOException {

        String blobDir = OBJECTS_DIR + "/" + subDir;
        Files.createDirectories(Paths.get(blobDir)); // Create subdirectories if they don't exist

        // Write the blob data to a file
        Files.write(Paths.get(blobDir + "/" + fileName), blob.getContent());
    }

}
