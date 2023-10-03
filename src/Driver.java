package src;

import java.util.Scanner;
import java.lang.Math;

public class Driver {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        String fileName = IO.readfileName();
        IO io = new IO(fileName);
        Matrix m = io.readMatrixFromFile();
        m.displayMatrix();
        // double res = Math.pow(0, -1);
        // System.out.println(res);
    }
}
