package src;

import java.io.File;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InterpolasiBikubik {
    
    public static void mainInterpolasiBikubik(String[] args) {
        // Variable declaration
        String fileName = IO.readfileName();
        IO f = new IO(fileName);
        double[] temp = f.readBicubicSpline();
        Matrix M = new Matrix(16, 1);
        Matrix X = new Matrix(16, 16);
        Matrix res = new Matrix(16, 1);
        Matrix invX = new Matrix(16, 16);
        double askX, askY, askRes = 0;

        // Construct Matrix X
        constructX(X);

        // Read input from file
        for (int i = 0; i < 16; i++) {
            M.setElmt(i, 0, temp[i]);
        }
        askX = temp[16];
        askY = temp[17];

        // Inverse X and multiply with M
        invX.copyMatrix(X);
        boolean isInvertible = Matrix.InversWithGaussJordan(invX);
        res = Matrix.multiplyMatrix(invX, M);

        // solve for x and y
        int idx = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                askRes += res.getElmt(idx, 0) * Math.pow(askX, i) * Math.pow(askY, j);
                idx++;
            }
        }
        System.out.println();
        System.out.println("f(" + askX + "," + askY + ") = " + askRes);
        System.out.println();
        System.out.println(">> Apakah ingin menyimpan keluaran?");
        System.out.println(">> 1. Ya");
        System.out.println(">> 2. Tidak");
        System.out.print(">> Masukkan pilihan: ");
        int choice = IO.iinput.nextInt();
        if (choice == 1){
            String[] s = new String[1];
            s[0] = "f(" + askX + "," + askY + ") = " + askRes;
            f.writeStringToFile(s);
        }
    }

    // Construct Matrix X
    public static void constructX(Matrix X) {
        int row = 0, col = 0;
        // f(x, y)
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                col = 0;
                for (int j = 0; j < 4; j++) {
                    for (int i = 0; i < 4; i++) {
                        X.setElmt(row, col, Math.pow(x, i) * Math.pow(y, j));
                        col++;
                    }
                }
                row++;
            }
        }
        // fx(x, y);
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                col = 0;
                for (int j = 0; j < 4; j++) {
                    for (int i = 0; i < 4; i++) {
                        if (i != 0)
                            X.setElmt(row, col, i * Math.pow(x, i - 1) * Math.pow(y, j));
                        else
                            X.setElmt(row, col, 0);
                        col++;
                    }
                }
                row++;
            }
        }
        // fy(x, y)
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                col = 0;
                for (int j = 0; j < 4; j++) {
                    for (int i = 0; i < 4; i++) {
                        if (j != 0)
                            X.setElmt(row, col, j * Math.pow(x, i) * Math.pow(y, j - 1));
                        else
                            X.setElmt(row, col, 0);
                        col++;
                    }
                }
                row++;
            }
        }
        // fxy(x, y)
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                col = 0;
                for (int j = 0; j < 4; j++) {
                    for (int i = 0; i < 4; i++) {
                        if (i != 0 && j != 0)
                            X.setElmt(row, col, i * j * Math.pow(x, i - 1) * Math.pow(y, j - 1));
                        else
                            X.setElmt(row, col, 0);
                        col++;
                    }
                }
                row++;
            }
        }
    }
}