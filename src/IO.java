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


    // Open & Close File
    public void openFile(){
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

    // Read File
    public int getRow(){
        int cnt = 0;
        openFile();
        while (sc.hasNextLine()){
            sc.nextLine();
            cnt++;
        }
        closeFile();
        return cnt;
    }

    public int getCol(){
        int cnt = 0;
        openFile();
        Scanner scCol = new Scanner(sc.nextLine());
        while (scCol.hasNextDouble()){
            cnt++;
            scCol.nextDouble();
        }
        scCol.close();
        closeFile();
        return cnt;
    }

    public Matrix readMatrixFromFile(){
        int row = getRow();
        int col = getCol();
        Matrix res = new Matrix(row, col);
        openFile();
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                res.setElmt(i, j, sc.nextDouble());
            }
        }
        closeFile();
        return res;
    }

    public Matrix readPointFromFile(){
        int row = getRow();
        Matrix res = new Matrix(row, 2);
        openFile();
        for (int i = 0; i < row; i++){
            for (int j = 0; j < 2; j++){
                res.setElmt(i, j, sc.nextDouble());
            }
        }
        closeFile();
        return res;
    }

    public double[] readBicubicSpline(){
        double[] res = new double[18];
        openFile();
        for (int i = 0; i < 18; i++){
            res[i] = sc.nextDouble();
        }
        closeFile();
        return res;
    }

    // Write
    public void writeBicubicSpline(double res){
        try {
            String namaFile = "C:\\Users\\adril\\ITB Files\\Semester III\\Linear and Geometry Algebra\\Tubes\\Tubes 1\\Algeo01-22037\\test\\bicubic.txt";
            File file = new File(namaFile);
            file.createNewFile();
            FileWriter write = new FileWriter(namaFile);
            write.write(Double.toString(res));
            write.close();
        } catch (Exception e) {
            System.out.println("error");
            // TODO: handle exception
        }
    }
}
