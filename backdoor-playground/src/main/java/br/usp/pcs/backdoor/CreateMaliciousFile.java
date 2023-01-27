package br.usp.pcs.backdoor;

import java.io.File;
import java.io.IOException;

public class CreateMaliciousFile {
    private static final String FULL_PATH = "filename.txt";

    public CreateMaliciousFile(){}

    public static boolean createFile(){
        if(verifyExists()){
            return true;
        }

        try {
            File myObj = new File(FULL_PATH);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteFile(){
        if(!verifyExists()){
            return true;
        }

        File myObj = new File(FULL_PATH);
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
        return true;
    }

    public static boolean verifyExists(){
        File f = new File(FULL_PATH);
        return f.exists();
    }
}
