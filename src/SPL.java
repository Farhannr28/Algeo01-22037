package src;

public class SPL {
    public static Matrix cramer(Matrix m) {
        Matrix res, coef;
        res = new Matrix(m.getRow(), 1);
        coef = Matrix.getCoefficient(m);
        // double coefDet = Matrix.determinantWithCofactor(coef);
        double coefDet = Matrix.determinantWithOBE(coef);
        for (int i = 0; i < m.getCol() - 1; i++) {
            coef = Matrix.getCoefficient(m);
            for (int j = 0; j < m.getRow(); j++) {
                coef.setElmt(j, i, m.getElmt(j, m.getCol() - 1));
            }
            // res.setElmt(i, 0, Matrix.determinantWithCofactor(coef) / coefDet);
            res.setElmt(i, 0, Matrix.determinantWithOBE(coef) / coefDet);
        }
        res.negatedZero();
        return res;
    }

    public static boolean isSolExist(Matrix m)
    // Mengirim true jika SPL memiliki solusi
    {
        int i;
        boolean Sol_exist = true;
        i = m.getRow() - 1;
        while (Sol_exist == true && i >= 0) {
            if (m.noSolutions(i)) {
                Sol_exist = false;
            }
            i--;
        }
        return Sol_exist;
    }

    public static boolean isHasilHomogen(Matrix m) {
        // Mengirimkan true jika hasil spl adalah homogen
        boolean ans;
        ans = true;
        for (int i = 0; i < m.getRow(); i++) {
            if (m.getElmt(i, 0) != 0) {
                ans = false;
            }
        }
        return ans;
    }

    public static boolean isParametrikSolution(Matrix m, int col)
    // Mengirim true jika pada suatu kolom matriks tidak ada yang jadi leading one
    {
        double nZero;
        int idx;
        int i;
        for (i = 0; i < m.getRow(); i++) {
            nZero = m.getNotZero(i);
            idx = m.getColIdx(i, nZero);
            if (m.getElmt(i, col) == 1 && idx == col) {
                return false;
            }
        }
        return true;
    }
    // public static boolean isParametricSolution(Matrix m, int col);

    public static String[] gaussElimination(Matrix m)
    // Mendapatkan solusi SPL dengan metode Eliminasi Gauss
    {
        m.getEselonBaris(); // mendapatkan matrix eselon baris

        String[] Solution;
        if (isSolExist(m)) // SPL memiliki solusi
        {
            Matrix Row_Temp;
            Solution = new String[m.getlastColIdx()];
            Row_Temp = new Matrix(m.getRow(), m.getCol());

            int i, j;
            for (i = m.getlastRowIdx(); i >= 0; i--) {
                double notzero = m.getNotZero(i);
                int idx_col = m.getColIdx(i, notzero);
                // mencari solusi dari baris yang bukan 0 semua
                if (idx_col != (-1)) {
                    for (j = 0; j < m.getCol(); j++) {
                        Row_Temp.Mat[i][j] = m.getElmt(i, j);
                    }
                    // Eliminasi dengan penyulihan mundur
                    int k;
                    for (k = idx_col + 1; k < m.getlastColIdx(); k++) {
                        if (!isParametrikSolution(m, k)) {
                            boolean find = false;
                            j = 0;
                            int idx, idx_temp = -1;
                            while (j < m.getCol() && find == false) {
                                idx = Row_Temp.getColIdx(j, Row_Temp.getNotZero(j));
                                if (idx == k) {
                                    idx_temp = j;
                                    find = true;
                                }
                                j++;
                            }

                            double multiply = Row_Temp.getElmt(i, k);
                            for (j = k; j < m.getCol(); j++) {
                                Row_Temp.Mat[i][j] = Row_Temp.getElmt(i, j) - multiply * Row_Temp.getElmt(idx_temp, j);
                            }
                        }
                    }

                    // Proses meng-assign list Solution dengan eliminasi tiap baris pada Row_Temp
                    if (Row_Temp.getElmt(i, m.getlastColIdx()) == 0.0) {
                        // Mencari apakah ada variabel lain yang akan disubstitusi di belakangnya
                        int a, ada = 0;
                        for (a = idx_col + 1; a < m.getlastColIdx(); a++) {
                            if (Row_Temp.getElmt(i, a) != 0) {
                                ada++;
                            }
                        }
                        if (ada > 0) {
                            Solution[idx_col] = "X" + (idx_col + 1) + " = ";
                        } else {
                            Solution[idx_col] = "X" + (idx_col + 1) + " = " + Row_Temp.getElmt(i, m.getlastColIdx())
                                    + " ";
                        }
                    } else {
                        Solution[idx_col] = "X" + (idx_col + 1) + " = " + Row_Temp.getElmt(i, m.getlastColIdx()) + " ";
                    }
                    int exist = 0;
                    for (k = idx_col + 1; k < m.getlastColIdx(); k++) {
                        if (isParametrikSolution(m, k)) {
                            if (Solution[k] == null) {
                                Solution[k] = "X" + (k + 1) + " = " + "t" + (k + 1) + ", dengan " + "t" + (k + 1)
                                        + " bilangan Real.";
                            }

                            if (Row_Temp.getElmt(i, k) != 0) {
                                if (Row_Temp.getElmt(i, k) < 0) {
                                    if (Row_Temp.getElmt(i, Row_Temp.getlastColIdx()) != 0 || exist > 0) {
                                        Solution[idx_col] += "+ ";
                                    }

                                    if (Row_Temp.getElmt(i, k) != -1) {
                                        Solution[idx_col] += (-1) * Row_Temp.getElmt(i, k) + " " + "t" + (k + 1) + " ";
                                    } else { // Row_Temp.getElmt(i, k) == -1
                                        Solution[idx_col] += "t" + (k + 1) + " ";
                                    }

                                } else {
                                    if (Row_Temp.getElmt(i, k) != 1) {
                                        Solution[idx_col] += "- " + Row_Temp.getElmt(i, k) + " " + "t" + (k + 1) + " ";
                                    } else { // Row_Temp.getElmt(i, k) == 1
                                        Solution[idx_col] += "- " + "t" + (k + 1) + " ";
                                    }
                                }
                                exist++;
                            }
                        }
                    }
                }
            }

            for (i = 0; i < m.getlastColIdx(); i++) {
                // handling kasus solusi tidak tunggal dalam parametrik yang belum terassign
                // karena index di luar looping
                if (Solution[i] == null) {
                    Solution[i] = "X" + (i + 1) + " = " + "t" + (i + 1) + ", dengan " + "t" + (i + 1)
                            + " bilangan Real.";
                }
            }
        } else {// TIdak ada solusi, isSolExist(m) == false
            Solution = new String[1];
            Solution[0] = "Sistem persamaan linear tidak memiliki solusi";
        }
        return Solution;
    }

    public static String[] gaussJordanElimination(Matrix m)
    // Mendapatkan solusi SPL dengan metode Eliminasi Gauss
    {
        m.getEselonBarisTereduksi();

        String[] Solution;
        if (isSolExist(m)) {
            Solution = new String[m.getlastColIdx()];

            int i = 0;
            int idx_col = 0;
            while (idx_col != -1 && i < m.getRow()) {
                double notzero = m.getNotZero(i);
                idx_col = m.getColIdx(i, notzero);

                if (idx_col != -1) {
                    if (m.getElmt(i, m.getlastColIdx()) == 0.0) {
                        int a, ada = 0;
                        for (a = idx_col + 1; a < m.getlastColIdx(); a++) {
                            if (m.getElmt(i, a) != 0) {
                                ada++;
                            }
                        }
                        if (ada > 0) {
                            Solution[idx_col] = "X" + (idx_col + 1) + " = ";
                        } else {
                            Solution[idx_col] = "X" + (idx_col + 1) + " = " + m.getElmt(i, m.getlastColIdx()) + " ";
                        }
                    } else {
                        Solution[idx_col] = "X" + (idx_col + 1) + " = " + m.getElmt(i, m.getlastColIdx()) + " ";
                    }

                    int exist = 0;
                    int j;
                    for (j = idx_col + 1; j < m.getlastColIdx(); j++) {
                        if (m.getElmt(i, j) != 0 && isParametrikSolution(m, j)) {
                            if (Solution[j] == null) {
                                Solution[j] = "X" + (j + 1) + " = " + "t" + (j + 1) + ", dengan " + "t" + (j + 1)
                                        + " bilangan Real.";
                            }

                            if (m.getElmt(i, j) < 0) {
                                if (m.getElmt(i, m.getlastColIdx()) != 0 || exist > 0) {
                                    Solution[idx_col] += "+ ";
                                }

                                if (m.getElmt(i, j) != -1) {
                                    Solution[idx_col] += (-1) * m.getElmt(i, j) + " " + "t" + (j + 1) + " ";
                                } else { // m.getElmt(i, j) == -1
                                    Solution[idx_col] += "t" + (j + 1) + " ";
                                }

                            } else {
                                if (m.getElmt(i, j) != 1) {
                                    Solution[idx_col] += "- " + m.getElmt(i, j) + " " + "t" + (j + 1) + " ";
                                } else { // m.getElmt(i, j) == 1
                                    Solution[idx_col] += "- " + "t" + (j + 1) + " ";
                                }
                            }
                            exist++;
                        }
                    }
                }
                i++;
            }

            for (i = 0; i < m.getlastColIdx(); i++) {
                // handling kasus solusi tidak tunggal dalam parametrik yang belum terassign
                // karena index di luar looping
                if (Solution[i] == null) {
                    Solution[i] = "X" + (i + 1) + " = " + "t" + (i + 1) + ", dengan " + "t" + (i + 1)
                            + " bilangan Real.";
                }
            }
        } else {// TIdak ada solusi, isSolExist(m) == false
            Solution = new String[1];
            Solution[0] = "Sistem persamaan linear tidak memiliki solusi";
        }
        return Solution;
    }

    public static Matrix metodeBalikan(Matrix m) {
        Matrix ans = new Matrix(m.getRow(), 1);
        Matrix b = new Matrix(m.getRow(), 1);
        Matrix a = new Matrix(m.getRow(), m.getCol() - 1);
        a = Matrix.getCoefficient(m);
        for (int i = 0; i < m.getRow(); i++) {
            b.Mat[i][0] = m.Mat[i][m.getCol() - 1];
        }
        if (isHasilHomogen(b)) {
            if (Matrix.InversWithCofactor(a)) {
                for (int i = 0; i < m.getRow(); i++) {
                    ans.Mat[i][0] = 0;
                }
            }
            // else tidak dapat menggunakan balikan
        } else {
            if (Matrix.InversWithCofactor(a)) {
                ans = Matrix.multiplyMatrix(a, b);
            }
            // else tidak dapat menggunakan balikan
        }
        return ans;
    }
}