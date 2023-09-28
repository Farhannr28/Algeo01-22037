package src;

import java.util.Scanner;

public class Interpolasi {
    static Scanner input = new Scanner(System.in);

    public static Matrix readmPointKeyboard()
    // Membaca n sebagai derajat polinom, dan n+1 buah point
    // Mengirim n+1 buah point dalam bentuk matrix
    {
        // Input derajat polinomial
        int n;
        System.out.print("n: ");
        n = input.nextInt();

        // Input titik titik sebanyak n+1 point
        Matrix mPoint;
        mPoint = new Matrix(n + 1, 2);
        int i, j;
        for (i = 0; i < mPoint.getRow(); i++) {
            System.out.print("(x" + i + ", y" + i + "): ");
            for (j = 0; j < mPoint.getCol(); j++) {
                mPoint.Mat[i][j] = input.nextDouble();
            }
        }
        return mPoint;
    }

    public static double[] getKoefPolinomial(Matrix m) {
        Matrix mPolinom;
        mPolinom = new Matrix(m.getRow(), m.getRow() + 1);
        int i, j;
        for (i = 0; i < mPolinom.getRow(); i++) {
            for (j = 0; j < mPolinom.getlastColIdx(); j++) {
                mPolinom.Mat[i][j] = Math.pow(m.getElmt(i, 0), j);
            }
            mPolinom.Mat[i][mPolinom.getlastColIdx()] = m.getElmt(i, 1);
        }

        mPolinom.getEselonBarisTereduksi();
        double[] koef;
        koef = new double[mPolinom.getlastColIdx()];
        for (i = 0; i < mPolinom.getlastColIdx(); i++) {
            koef[i] = mPolinom.getElmt(i, mPolinom.getlastColIdx());
        }
        return koef;
    }

    public static double getRangeInterpolasi(double[] koef, double x) {
        double result = 0;
        int i;
        for (i = 0; i < koef.length; i++) {
            result += koef[i] * Math.pow(x, i);
        }
        return result;
    }

    public static String persamaanInterpolasi(double[] koef) {
        String result;
        result = "f(x) = ";
        int exist = 0;
        int i;
        for (i = koef.length - 1; i > 0; i--) {
            if (koef[i] != 0) {
                if (koef[i] > 0) {
                    if (exist > 0) {
                        result += "+ ";
                    }
                    if (koef[i] == 1) {
                        result += "x^" + i + " ";
                    } else { // koef[i] != 1
                        result += koef[i] + " x^" + i + " ";
                    }
                } else { // (koef[i] < 0)
                    if (koef[i] == -1) {
                        result += "- x^" + i + " ";
                    } else {
                        result += "- " + (-1) * koef[i] + " x^" + i + " ";
                    }
                }
                exist++;
            }
        }
        if (koef[i] < 0) {
            result += "- " + (-1) * koef[0];
        } else if (koef[i] > 0) {
            if (exist > 0) {
                result += "+ " + koef[0];
            } else {
                result += "" + koef[0];
            }
        } else {
            if (exist == 0) {
                result += "" + koef[0];
            }
        }
        return result;
    }
}