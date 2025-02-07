package com.elitekaycy.gitvc.utils.object;

public class Commit {
    private String id;
    private String date;
    private String message;
    private String file;
    private Commit Parent;

    public Commit() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Commit {\n");
        res.append("  Id: ").append(id).append("\n");
        res.append("  Date: ").append(date).append("\n");
        res.append("  Message: ").append(message).append("\n");
        res.append("  File: ").append(file).append("\n");
        if (Parent != null) {
            res.append("  Parent: ").append(Parent.getHash()).append("\n");
        } else {
            res.append("  Parent: null\n");
        }
        res.append("}");
        return res.toString();
    }

}
