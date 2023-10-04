package src;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Interpolasi {
    static Scanner input = new Scanner(System.in);

    public static Matrix readmPointKeyboard()
    // Membaca n sebagai derajat polinom, dan n+1 buah point
    // Mengirim n+1 buah point dalam bentuk matrix
    {
        // Input derajat polinomial
        int n;

        System.out.print(">> Masukkan derajat polinom(n): ");
        n = input.nextInt();

        // Input titik titik sebanyak n+1 point
        Matrix mPoint;
        mPoint = new Matrix(n + 1, 2);
        int i, j;
        for (i = 0; i < mPoint.getRow(); i++) {
            System.out.print(">> (x" + i + ", y" + i + "): ");
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
                        if (i != 1)
                            result += "x^" + i + " ";
                        else
                            result += "x ";
                    } else { // koef[i] != 1
                        if (i != 1)
                            result += koef[i] + " x^" + i + " ";
                        else
                            result += koef[i] + " x ";
                    }
                } else { // (koef[i] < 0)
                    if (koef[i] == -1) {
                        if (i != 1)
                            result += "- x^" + i + " ";
                        else
                            result += "- x ";
                    } else {
                        if (i != 1)
                            result += "- " + (-1) * koef[i] + " x^" + i + " ";
                        else
                            result += "- " + (-1) * koef[i] + " x ";
                    }
                }
                exist++;
            }
        }
        if (koef[i] < 0) {
            result += "- " + (-1) * koef[i] ;
        } else if (koef[i] > 0) {
            if (exist > 0) {
                result += "+ " + koef[i];
            } else {
                result += "" + koef[i];
            }
        } else {
            if (exist == 0) {
                result += "" + koef[i] ;
            }
        }
        return result;
    }

    public static void main(String[] args) {

        IO f = new IO("");
        System.out.println(">> Pilih tipe masukan");
        System.out.println(">> 1. Keyboard");
        System.out.println(">> 2. File");
        int Masukan;
        do {
            System.out.print(">> Masukkan pilihan:  ");
            Masukan = input.nextInt();
        } while (Masukan != 1 && Masukan != 2);
        System.out.println();

        Matrix m;
        double x = 0;
        if (Masukan == 1) {
            m = readmPointKeyboard();
            System.out.print(">> Masukkan nilai untuk diaproksimasi (x): ");
            x = input.nextDouble();
            System.out.println();
        } else {
            String fileName = IO.readfileName();
            f = new IO(fileName);
            Matrix temp = f.readPointFromFile();
            m = new Matrix(temp.getRow() - 1, 2);
            for (int i = 0; i < temp.getRow() - 1; i++){
                for (int j = 0; j < 2; j++){
                    m.setElmt(i, j, temp.getElmt(i, j));
                }
            }
            x = temp.getElmt(temp.getRow() - 1, 0);
        }

        double[] koef = getKoefPolinomial(m);
        String equation = persamaanInterpolasi(koef);
        double range = getRangeInterpolasi(koef, x);
        String output = equation + ", f(" + x + ") = " + range;

        System.out.println(output);
        System.out.println();
        System.out.println(">> Ingin menyimpan hasil?");
        System.out.println(">> 1. Ya");
        System.out.println(">> 2. Tidak");
        int isSave;
        do {
            System.out.print(">> Masukkan pilihan: ");
            isSave = input.nextInt();
        } while (isSave != 1 && isSave != 2);
        System.out.println();
        if (isSave == 1) {
            try {
                String[] res = new String[1];
                res[0] = output;
                f.writeStringToFile(res);
            } catch (Exception e) {
                System.out.println("error");
                // TODO: handle exception
            }
        }
    }
}