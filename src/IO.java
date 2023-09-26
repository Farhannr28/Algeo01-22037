package src;

import java.util.*;
import java.io.*;

public class IO {
    private String fileName;
    private Scanner sc;

    // Constructor
    public IO(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return this.fileName;
    }


    // Read File
    public void readFile(){
        try {
            File file = new File(getFileName());
            this.sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("There is no file with name " + getFileName());
            System.exit(0);
        }
    }
    public void closeFile(){
        sc.close();
    }
}
