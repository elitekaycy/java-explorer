package com.elitekaycy.gitvc.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import com.elitekaycy.gitvc.utils.object.Commit;
import com.elitekaycy.gitvc.utils.object.GitBlob;

public class Git {

    private final String GIT_DIR = ".vcs";
    private final Path repoDir = Paths.get(".vcs");
    private final Path COMMIT_FILE = Paths.get(".vcs", "commit");
    private final Path INDEX_FILE = Paths.get(".vcs", "staging");
    private final Path OBJECT_DIR = Paths.get(".vcs", "objects");

    public Git() {
    }

    @Override
    public void init() {
        if (!Files.exists(repoDir)) {

            Files.createDirectories(repoDir);
            Files.createDirectories(Paths.get(".vcs/branches"));
            Files.createDirectories(Paths.get(".vcs/commits"));
            Files.createDirectories(Paths.get(".vcs/staging"));
            Files.createDirectories(Paths.get(".vcs/config"));
            Files.createDirectories(Paths.get(".vcs/objects"));
            Files.createDirectories(Paths.get(".vcs/refs/heads"));

            Files.write(Paths.get(repoDir, "HEAD"), "ref: refs/heads/master".getBytes());

            System.out.println("Initialized empty repository in " + repoDir);
        }
    }

    @Override
    public void add(String filePath) {

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File does not exist.");
                return;
            }

            GitBlob blob = new GitBlob(filePath);
            byte[] content = blob.getContent();
            String hash = blob.getHash();

            File objectFile = new File(GIT_DIR + "/objects/" + hash);
            if (!objectFile.exists()) {
                Files.write(objectFile.toPath(), content);
            }

            List<String> indexLines = new ArrayList<>();
            if (new File(INDEX_FILE).exists()) {
                indexLines = Files.readAllLines(INDEX_FILE);
            }
            indexLines.add(fileName + " " + hash);
            Files.write(Paths.get(INDEX_FILE), indexLines);

            System.out.println("File added: " + fileName + " -> " + hash);

            // create blob to object directory
            // add fs and blob string to staging
            // replace what exists if exists
            //
            // add only if filepath exist
            // store blob at ./vcs/objects/
            // ./vcs/staging should be updated to keep track of the blob and fs
        } catch (IOException ex) {
            ex.printTrackStrace();
        }
    }

    public void commit(String message) {

        List<String> indexLines = Files.readAllLines(Paths.get(INDEX_FILE));
        if (indexLines.isEmpty()) {
            System.out.println("Nothing to commit.");
            return;
        }

        File commitFile = new File(OBJECTS_DIR + "/" + commitHash);
        Commit previousCommit = Commit.get(id);
        String commitContent = "Commit: " + commitHash + "\nMessage: " + message + "\nDate: " + Instant.now()
                + "\nParent: " + previousCommit + "\nFiles:\n" + String.join("\n", indexLines);
        Commit curr = new Commit(
                message,
                Instant.now().toString(),
                previousCommit,
                String.join("\n", indexLines));

        Files.write(commitFile.toPath(), commitContent.getBytes());

        // Update HEAD
        Files.write(Paths.get(HEAD_FILE), commitHash.getBytes());

        // Clear index (staging area)
        new File(INDEX_FILE).delete();
        new File(INDEX_FILE).createNewFile();

        System.out.println("Committed as " + commitHash);
    }

}
