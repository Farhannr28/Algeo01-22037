package src;

import java.io.IOException;
import java.util.*;

public class Main {
    static Scanner input = new Scanner(System.in); // integer
    static Scanner scan = new Scanner(System.in); // string

    public static String opening = """

            =======================================================================================================================================



            /$$$$$$ /$$   /$$ /$$$$$$$$ /$$$$$$$$ /$$$$$$$  /$$   /$$ /$$$$$$$   /$$$$$$  /$$$$$$          /$$$$$  /$$$$$$  /$$      /$$  /$$$$$$
            |_  $$_/| $$$ | $$|__  $$__/| $$_____/| $$__  $$| $$  | $$| $$__  $$ /$$__  $$|_  $$_/         |__  $$ /$$__  $$| $$  /$ | $$ /$$__  $$
              | $$  | $$$$| $$   | $$   | $$      | $$  \\ $$| $$  | $$| $$  \\ $$| $$  \\__/  | $$              | $$| $$  \\ $$| $$ /$$$| $$| $$  \\ $$
              | $$  | $$ $$ $$   | $$   | $$$$$   | $$$$$$$/| $$  | $$| $$$$$$$/|  $$$$$$   | $$              | $$| $$$$$$$$| $$/$$ $$ $$| $$$$$$$$
              | $$  | $$  $$$$   | $$   | $$__/   | $$__  $$| $$  | $$| $$____/  \\____  $$  | $$         /$$  | $$| $$__  $$| $$$$_  $$$$| $$__  $$
              | $$  | $$\\  $$$   | $$   | $$      | $$  \\ $$| $$  | $$| $$       /$$  \\ $$  | $$        | $$  | $$| $$  | $$| $$$/ \\  $$$| $$  | $$
             /$$$$$$| $$ \\  $$   | $$   | $$$$$$$$| $$  | $$|  $$$$$$/| $$      |  $$$$$$/ /$$$$$$      |  $$$$$$/| $$  | $$| $$/   \\  $$| $$  | $$
            |______/|__/  \\__/   |__/   |________/|__/  |__/ \\______/ |__/       \\______/ |______/       \\______/ |__/  |__/|__/     \\__/|__/  |__/



            =======================================================================================================================================

                        """;

    public static void printOpening() {
        System.out.print(opening);
    }

    public static void printMenu() {
        System.out.println(">> MENU");
        System.out.print("""
                >> 1. Sistem Persamaaan Linier
                >> 2. Determinan
                >> 3. Matriks balikan
                >> 4. Interpolasi Polinom
                >> 5. Interpolasi Bicubic Spline
                >> 6. Regresi linier berganda
                >> 7. Interpolasi Gambar
                >> 8. Keluar
                    """);
    }

    public static void printInputType() {
        System.out.print("""
                >> Choose Input Type
                >> 1. Keyboard
                >> 2. File
                """);
    }

    public static void printSPLChoice() {
        System.out.print("""
                >> Choose Method
                >> 1. Metode Eliminasi Gausss
                >> 2. Metode Eliminasi Gauss-Jordan
                >> 3. Metode Matriks Balikan
                >> 4. Metode Cramer
                """);
    }

    public static void printDeterminantChoice() {
        System.out.print("""
                >> Choose Method
                >> 1. Metode Ekpansi Kofaktor
                >> 2. Metode OBE
                """);
    }

    public static void printInverseChoice() {
        System.out.print("""
                >> Choose Method
                >> 1. Metode Gauss-Jordan
                >> 2. Metode Adjoin
                """);
    }

    public static void printEnterChoice() {
        System.out.print(">> Enter choice: ");
    }

    public static void pressEnterToContinue() {
        System.out.print(">> Press Enter key to continue...");
        scan.nextLine();
    }

    public static void printOutputChoice() {
        System.out.print("""
                >> Choose Output Type
                >> 1. Terminal
                >> 2. Save to file
                """);
    }

    public static void clearScreen() throws InterruptedException, IOException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    public static void printInputInvalid() {
        System.out.println(">> Input invalid");
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Matrix temp;
        boolean run = true;
        int choice, inputType, outputType, row, col;
        String fileName;
        String[] res = new String[1];
        double det;
        IO io = new IO("");
        Matrix m;
        while (run) {
            clearScreen();
            printOpening();
            printMenu();
            printEnterChoice();
            choice = input.nextInt();
            switch (choice) {
                case 1 -> {
                    clearScreen();
                    printOpening();
                    printSPLChoice();
                    printEnterChoice();
                    choice = input.nextInt();
                    System.out.println("");
                    if (choice < 1 || choice > 4) {
                        printInputInvalid();
                        break;
                    } else {
                        printInputType();
                        printEnterChoice();
                        inputType = input.nextInt();
                        System.out.println("");
                        if (inputType == 1) {
                            System.out.print(">> Enter number of row: ");
                            row = input.nextInt();
                            System.out.print(">> Enter number of column: ");
                            col = input.nextInt();
                            m = new Matrix(row, col);
                            System.out.println(">> Enter Matrix");
                            m.readMatrix();
                            System.out.println("");
                        } else {
                            fileName = IO.readfileName();
                            io = new IO(fileName);
                            System.out.println("");
                            m = io.readMatrixFromFile();
                        }
                        switch (choice) {
                            case 1 -> {
                                res = SPL.gaussElimination(m);
                                break;
                            }
                            case 2 -> {
                                res = SPL.gaussJordanElimination(m);
                                break;
                            }
                            case 3 -> {
                                if (m.getRow() != m.getCol() - 1) {
                                    res = new String[1];
                                    res[0] = "Inverse method failed because the matrix is not square";
                                } else {
                                    det = Matrix.determinantWithCofactor(Matrix.getCoefficient(m));
                                    if (det == 0) {
                                        res = new String[1];
                                        res[0] = "Inverse method failed because the matrix' determinant is 0";
                                    } else {
                                        temp = SPL.metodeBalikan(m);
                                        res = new String[m.getRow()];
                                        for (int i = 0; i < temp.getRow(); i++) {
                                            res[i] = Double.toString(temp.getElmt(i, 0));
                                        }
                                    }
                                }
                                break;
                            }
                            default -> {
                                if (m.getRow() != m.getCol() - 1) {
                                    res = new String[1];
                                    res[0] = "Cramer's rule failed because the matrix is not square";
                                } else {
                                    det = Matrix.determinantWithCofactor(Matrix.getCoefficient(m));
                                    if (det == 0) {
                                        res = new String[1];
                                        res[0] = "Cramer's rule failed because the matrix' determinant is 0";
                                    } else {
                                        temp = SPL.cramer(m);
                                        res = new String[m.getRow()];
                                        for (int i = 0; i < temp.getRow(); i++) {
                                            res[i] = Double.toString(temp.getElmt(i, 0));
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        printOutputChoice();
                        printEnterChoice();
                        outputType = input.nextInt();
                        System.out.println("");
                        if (outputType == 1) {
                            for (int i = 0; i < res.length; i++) {
                                System.out.println(res[i]);
                            }
                        } else {
                            io.writeStringToFile(res);
                        }
                        pressEnterToContinue();
                    }
                    break;
                }
                case 2 -> {
                    clearScreen();
                    printOpening();
                    printDeterminantChoice();
                    printEnterChoice();
                    choice = input.nextInt();
                    System.out.println("");
                    if (choice < 1 || choice > 2) {
                        printInputInvalid();
                        break;
                    } else {
                        printInputType();
                        printEnterChoice();
                        inputType = input.nextInt();
                        System.out.println("");
                        if (inputType == 1) {
                            System.out.print(">> Enter number of row: ");
                            row = input.nextInt();
                            System.out.print(">> Enter number of column: ");
                            col = input.nextInt();
                            m = new Matrix(row, col);
                            System.out.println(">> Enter Matrix");
                            m.readMatrix();
                            System.out.println("");
                        } else {
                            fileName = IO.readfileName();
                            io = new IO(fileName);
                            System.out.println("");
                            m = io.readMatrixFromFile();
                        }
                        if (choice == 1) {
                            if (m.getRow() != m.getCol()) {
                                res = new String[1];
                                res[0] = "Determinant undefined because the matrix is not square";
                            } else {
                                res = new String[1];
                                res[0] = Double.toString(Matrix.determinantWithCofactor(m));
                            }
                        } else {
                            if (m.getRow() != m.getCol()) {
                                res = new String[1];
                                res[0] = "Determinant undefined because the matrix is not square";
                            } else {
                                res = new String[1];
                                res[0] = Double.toString(Matrix.determinantWithOBE(m));
                            }
                        }
                        printOutputChoice();
                        printEnterChoice();
                        outputType = input.nextInt();
                        System.out.println("");
                        if (outputType == 1) {
                            for (int i = 0; i < res.length; i++) {
                                System.out.println(res[i]);
                            }
                        } else {
                            io.writeStringToFile(res);
                        }
                        pressEnterToContinue();
                    }
                    break;
                }
                case 3 -> {
                    clearScreen();
                    printOpening();
                    printInverseChoice();
                    printEnterChoice();
                    choice = input.nextInt();
                    System.out.println("");
                    if (choice < 1 || choice > 2) {
                        printInputInvalid();
                        break;
                    } else {
                        printInputType();
                        printEnterChoice();
                        inputType = input.nextInt();
                        System.out.println("");
                        if (inputType == 1) {
                            System.out.print(">> Enter number of row: ");
                            row = input.nextInt();
                            System.out.print(">> Enter number of column: ");
                            col = input.nextInt();
                            m = new Matrix(row, col);
                            System.out.println(">> Enter Matrix");
                            m.readMatrix();
                            System.out.println("");
                        } else {
                            fileName = IO.readfileName();
                            io = new IO(fileName);
                            System.out.println("");
                            m = io.readMatrixFromFile();
                        }
                        if (choice == 1) {
                            if (m.getRow() != m.getCol()) {
                                res = new String[1];
                                res[0] = "Inverse matrix undefined because the matrix is not square";
                            } else {
                                det = Matrix.determinantWithCofactor(m);
                                if (det == 0) {
                                    res = new String[1];
                                    res[0] = "Inverse matrix undefined because the matrix' determinant is 0";
                                } else {
                                    m.InversWithGaussJordan();
                                    res = new String[m.getRow()];
                                    for (int i = 0; i < m.getRow(); i++) {
                                        res[i] = "";
                                        for (int j = 0; j < m.getCol(); j++) {
                                            res[i] += Double.toString(m.getElmt(i, j));
                                            if (j != m.getCol() - 1)
                                                res[i] += " ";
                                        }
                                    }
                                }
                            }
                        } else {
                            if (m.getRow() != m.getCol()) {
                                res = new String[1];
                                res[0] = "Inverse matrix undefined because the matrix is not square";
                            } else {
                                det = Matrix.determinantWithCofactor(m);
                                if (det == 0) {
                                    res = new String[1];
                                    res[0] = "Inverse matrix undefined because the matrix' determinant is 0";
                                } else {
                                    Matrix.InversWithCofactor(m);
                                    res = new String[m.getRow()];
                                    for (int i = 0; i < m.getRow(); i++) {
                                        res[i] = "";
                                        for (int j = 0; j < m.getCol(); j++) {
                                            res[i] += Double.toString(m.getElmt(i, j));
                                            if (j != m.getCol() - 1)
                                                res[i] += " ";
                                        }
                                    }
                                }
                            }
                        }
                        printOutputChoice();
                        printEnterChoice();
                        outputType = input.nextInt();
                        System.out.println("");
                        if (outputType == 1) {
                            for (int i = 0; i < res.length; i++) {
                                System.out.println(res[i]);
                            }
                        } else {
                            io.writeStringToFile(res);
                        }
                        pressEnterToContinue();
                    }
                    break;
                }
                case 5->{
                    clearScreen();
                    printOpening();
                    InterpolasiBikubik.main(args);
                    pressEnterToContinue();
                    break;
                }
                case 7->{
                    clearScreen();
                    printOpening();
                    ImageInterpolation.main(args);
                    pressEnterToContinue();
                    break;
                }
                case 8->{
                    run = false;
                }
                default -> {
                    break;
                }
            }
        }
    }
}
