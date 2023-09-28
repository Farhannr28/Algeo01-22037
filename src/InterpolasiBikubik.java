package src;

import java.io.File;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InterpolasiBikubik {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int choice;
        // Variable declaration
        String fileName = readileName();
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

        // Temporary Way To Solve Bicubic
        invX.copyMatrix(X);
        boolean isInvertible = invX.InversWithGaussJordan();
        res = Matrix.multiplyMatrix(invX, M);

        // solve for x and y
        int idx = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                askRes += res.getElmt(idx, 0) * Math.pow(askX, i) * Math.pow(askY, j);
                idx++;
            }
        }
        f.writeBicubicSpline(askRes);
        in.close();
        System.out.println(askRes);
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

    public static String readileName() {
        Scanner scanFile = new Scanner(System.in);
        Scanner sc;
        String fileName = "";
        while (true) {
            try {
                System.out.print("Enter file name: ");
                fileName = scanFile.nextLine();
                File file = new File(fileName);
                sc = new Scanner(file);
                break;
            } catch (FileNotFoundException e) {
                System.out.println("There is no file with name " + fileName);
            }
        }
        scanFile.close();
        return fileName;
    }
}