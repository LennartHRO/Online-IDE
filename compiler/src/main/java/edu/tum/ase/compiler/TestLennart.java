package edu.tum.ase.compiler;

public class TestLennart {

    public static void main(String[] args) {
        String fileName1 = "Example.java";
        String fileName2 = "Sample.c";
        String fileName3 = "Document.txt";

        System.out.println(fileName1 + " is a " + getFileType(fileName1));
        System.out.println(fileName2 + " is a " + getFileType(fileName2));
        System.out.println(fileName3 + " is a " + getFileType(fileName3));
    }

    private static String getFileType(String fileName) {
        if (fileName.endsWith(".java")) {
            return "Java file";
        } else if (fileName.endsWith(".c")) {
            return "C file";
        } else {
            return "Neither Java nor C file";
        }
    }
}