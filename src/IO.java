package src;

import java.util.*;
import java.io.*;

public class IO {
    private String fileName;
    private Scanner sc; // for file
    public static Scanner scan = new Scanner(System.in); // for string
    public static Scanner input = new Scanner(System.in); // for double
    public static Scanner iinput = new Scanner(System.in); // for int

    // Constructor
    public IO(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    // Open & Close File
    public void openFile() {
        try {
            File file = new File(getFileName());
            this.sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println(">> Tidak ada file dengan nama " + getFileName());
            System.exit(0);
        }
    }

    public void closeFile() {
        sc.close();
    }

    // Read File
    public static String readfileName() {
        Scanner sc;
        scan = new Scanner(System.in);
        String fileName = "";
        while (true) {
            try {
                System.out.print(">> Masukkan nama file: ");
                fileName = scan.nextLine();
                File file = new File("test/" + fileName);
                sc = new Scanner(file);
                break;

            } catch (FileNotFoundException e) {
                System.out.println(">> Tidak ada file dengan nama " + fileName);
            }
        }
        // scan.close();
        sc.close();
        return "test/" + fileName;
    }

    public int getRow() {
        int cnt = 0;
        openFile();
        while (sc.hasNextLine()) {
            sc.nextLine();
            cnt++;
        }
        closeFile();
        return cnt;
    }

    public int getCol() {
        int cnt = 0;
        openFile();
        Scanner scCol = new Scanner(sc.nextLine());
        while (scCol.hasNextDouble()) {
            cnt++;
            scCol.nextDouble();
        }
        scCol.close();
        closeFile();
        return cnt;
    }

    public Matrix readMatrixFromFile() {
        int row = getRow();
        int col = getCol();
        Matrix res = new Matrix(row, col);
        openFile();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                res.setElmt(i, j, sc.nextDouble());
            }
        }
        closeFile();
        return res;
    }

    public Matrix readPointFromFile() {
        int row = getRow();
        Matrix res = new Matrix(row, 2);
        openFile();
        for (int i = 0; i < row - 1; i++) {
            for (int j = 0; j < 2; j++) {
                res.setElmt(i, j, sc.nextDouble());
            }
        }
        res.setElmt(row - 1, 0, sc.nextDouble());
        closeFile();
        return res;
    }

    public double[] readBicubicSpline() {
        double[] res = new double[18];
        openFile();
        for (int i = 0; i < 18; i++) {
            res[i] = sc.nextDouble();
        }
        closeFile();
        return res;
    }

    // Write
    public void writeBicubicSpline(String res) {
        try {
            System.out.print(">> Masukkan nama file keluaran: ");
            String namaFile = scan.nextLine();
            namaFile = "test/" + namaFile;
            File file = new File(namaFile);
            file.createNewFile();
            FileWriter write = new FileWriter(namaFile);
            write.write(res);
            write.close();
        } catch (Exception e) {
            System.out.println("error");
            // TODO: handle exception
        }
    }

    public void writeStringToFile(String[] res) {
        try {
            System.out.print(">> Masukkan nama file keluaran: ");
            String namaFile = scan.nextLine();
            namaFile = "test/" + namaFile;
            File file = new File(namaFile);
            file.createNewFile();
            FileWriter write = new FileWriter(namaFile);
            for (int i = 0; i < res.length; i++){
                write.write(res[i]);
                if (i != res.length - 1){
                    write.write("\n");
                }
            }
            write.close();
        } catch (Exception e) {
            System.out.println("error");
            // TODO: handle exception
        }
    }
}
