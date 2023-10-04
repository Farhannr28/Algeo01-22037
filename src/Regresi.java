package src;

import java.util.Scanner;

public class Regresi {
    static Scanner input = new Scanner(System.in);

    public static Matrix readRegresiKeyboard() {
        // Tabel M sampel N parameter (N kolom X, 1 kolom Y), dibaca menjadi matrix
        // Mx(N+2) dengan kolom terdepan berisi 1
        int n, m;
        System.out.print(">> Masukkan banyak sampel regresi (m): ");
        m = input.nextInt();
        System.out.print(">> Masukkan banyak peubah regresi (n):: ");
        n = input.nextInt();
        Matrix read = new Matrix(m, n);
        System.out.print(">> Masukkan isi matrix regresi (n x m): ");
        read.readMatrix();
        Matrix augment = new Matrix(m, n + 2);
        if (m != 1) {
            System.out.print(">> Masukkan nilai-nilai hasil fungsi (y1...y" + (m) + "): ");
        } else {
            System.out.print(">> Masukkan nilai hasil fungsi (y): ");
        }
        int i, j;
        double a;
        for (i = 0; i < m; i++) {
            a = input.nextDouble();
            augment.Mat[i][n + 1] = a;
        }
        for (i = 0; i < m; i++) {
            augment.Mat[i][0] = 1;
            for (j = 1; j < n + 1; j++) {
                augment.Mat[i][j] = read.Mat[i][j - 1];
            }
        }
        return augment;
    }

    public static Matrix readTaksir(Matrix m) {
        double a;
        Matrix taksir = new Matrix(1, m.getCol() - 1);
        taksir.Mat[0][0] = 1;
        System.out.print("Masukkan nilai-nilai yang ingin ditaksir hasil fungsinya (xk): ");
        for (int i = 1; i < taksir.getCol(); i++) {
            a = input.nextDouble();
            taksir.Mat[0][i] = a;
        }
        return taksir;
    }

    public static Matrix convertSPLMatrix(Matrix m) {
        // Diubah menjadi matrix SPL (N+1)X(N+2)
        Matrix SPL = new Matrix(m.getCol() - 1, m.getCol());
        Matrix tr = new Matrix(m.getCol(), m.getRow());
        tr = Matrix.transpose(m);
        // Matrix (N+2)X(M)
        double temp = 0;
        for (int i = 0; i < SPL.getRow(); i++) {
            for (int j = 0; j < SPL.getCol(); j++) {
                temp = 0;
                for (int k = 0; k < m.getRow(); k++) {
                    temp += (tr.Mat[i][k] * tr.Mat[j][k]);
                }
                SPL.Mat[i][j] = temp;
            }
        }
        return SPL;
    }

    public static String[] solusiSPL(Matrix m) {
        String sol[] = SPL.gaussElimination(m);
        return sol;
    }

    public static Matrix vektorRegresi(String[] solusi) {
        Matrix v = new Matrix(solusi.length, 1);
        int j;
        String temp;
        String num;
        if (!solusi[0].equals("Sistem persamaan linear tidak memiliki solusi")) {
            temp = solusi[0];
            j = 0;
            while (temp.charAt(j) != '=') {
                j++;
            }
            j += 2;
            num = "";
            while (j < temp.length() - 1) {
                num = num + temp.charAt(j);
                j++;
            }
            v.Mat[0][0] = Double.valueOf(num);
            for (int i = 1; i < solusi.length; i++) {
                temp = solusi[i];
                j = 0;
                while (temp.charAt(j) != '=') {
                    j++;
                }
                j += 2;
                num = "";
                while (j < temp.length() - 1) {
                    num = num + temp.charAt(j);
                    j++;
                }
                v.Mat[i][0] = Double.valueOf(num);
            }
        }
        return v;
    }

    public static double yRegresi(Matrix Xk, Matrix vektor) {
        // Mengembailkan hasil dari f(Xk), menggunakan matrix Xk berukuran 1x(N+1) dan
        // vektor regresi berukuran (N+1)x1
        Matrix M = Matrix.multiplyMatrix(Xk, vektor);
        return M.Mat[0][0];
    }

    public static String persamaanRegresi(String[] SPLSol, double taksir) {
        // Melakukan eliminasi gauss pada matrix SPL dan menghasilkan persamaan regresi
        String ans = "f(X) = ";
        String temp;
        String num;
        int j;
        if (!SPLSol[0].equals("Sistem persamaan linear tidak memiliki solusi")) {
            temp = SPLSol[0];
            j = 0;
            while (temp.charAt(j) != '=') {
                j++;
            }
            j += 2;
            num = "";
            while (j < temp.length() - 1) {
                num = num + temp.charAt(j);
                j++;
            }
            if (!(num.equals("0.0 ") || num.equals("-0.0 "))) {
                ans = ans + num;
            }
            for (int i = 1; i < SPLSol.length; i++) {
                temp = SPLSol[i];
                j = 0;
                while (temp.charAt(j) != '=') {
                    j++;
                }
                j += 2;
                num = "";
                while (j < temp.length()) {
                    num = num + temp.charAt(j);
                    j++;
                }
                if (!(num.equals("0.0 ") || num.equals("-0.0 "))) {
                    if (num.charAt(0) == '-') {
                        ans = ans + " - ";
                        if (!num.equals("-1.0 ")) {
                            for (j = 1; j < num.length(); j++) {
                                ans = ans + num.charAt(j);
                            }
                        }
                    } else {
                        ans = ans + " + ";
                        if (!num.equals("1.0 ")) {
                            ans = ans + num;
                        }
                    }
                    ans = ans + "X" + (i);
                }
            }
        }
        ans = ans + ", f(Xk) = " + taksir;
        return ans;
    }

    public static void main(String[] args) {
        String ans;
        String res[] = new String[1];
        Matrix augmented = readRegresiKeyboard();
        Matrix Xk = readTaksir(augmented);
        Matrix SPL = convertSPLMatrix(augmented);
        String[] sol;
        sol = solusiSPL(SPL);
        Matrix v = vektorRegresi(sol);
        // v.displayMatrix();
        ans = persamaanRegresi(sol, yRegresi(Xk, v));
        System.out.println(ans);
        res[0] = ans;
    }
}

/*
 * 1.0 1.0 1.0 0.0 3.0
 * 1.0 1.0 0.0 1.0 4.0
 * 1.0 0.0 1.0 1.0 5.0
 * 
 * 1.0 1.0 1.0
 * 1.0 1.0 0.0
 * 1.0 0.0 1.0
 * 0.0 1.0 1.0
 * 3.0 4.0 5.0
 * 
 * 3.0 2.0 2.0 2.0 12.0
 * 2.0 2.0 1.0 1.0 7.0
 * 2.0 1.0 2.0 1.0 8.0
 * 2.0 1.0 1.0 2.0 9.0
 * 
 * 4 - X1 + X3
 */