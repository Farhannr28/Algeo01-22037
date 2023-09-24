package src;

public class SPL {
    public static Matrix cramer(Matrix m) {
        Matrix res, coef;
        res = new Matrix(m.row, 1);
        coef = Matrix.getCoefficient(m);
        double coefDet = Matrix.determinantWithCofactor(coef);
        for (int i = 0; i < m.col - 1; i++) {
            coef = Matrix.getCoefficient(m);
            for (int j = 0; j < m.row; j++) {
                coef.Mat[j][i] = m.Mat[j][m.col - 1];
            }
            res.Mat[i][0] = Matrix.determinantWithCofactor(coef) / coefDet;
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

    public static boolean isParametrikSolution(Matrix m, int col)
    // Mengirim true jika pada suatu kolom matriks tidak ada yang jadi leading one baris
    {
        boolean found_leadOne = false;
        int j = 0, countO = 0;
        while (found_leadOne == false && j < m.getRow()) {
            if (m.getElmt(j, col) != 0) {
                int k = 0;
                while (k < col && found_leadOne == false) {
                    if (m.getElmt(j, k) != 0) {
                        found_leadOne = true;
                    }
                    k++;
                }
            } else {
                countO++;
            }
            j++;
        }
        return (found_leadOne || countO == m.getRow());
    }

    public static void gaussElimination(Matrix m)
    // Mendapatkan solusi SPL dengan metode Eliminasi Gauss
    {
        m.getEselonBaris(); // mendapatkan matrix eselon baris

        if (isSolExist(m)) // SPL memiliki solusi
        {
            char[] Parametrik = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                    'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' }; // karakter untuk solusi tidak tunggal
            String[] Solution;
            double[] Row_Temp;
            Solution = new String[m.getlastColIdx()];
            Row_Temp = new double[m.getCol()];

            int i, j;
            for (i = m.getlastRowIdx(); i >= 0; i--) {
                double notzero = m.getNotZero(i);
                int idx_col = m.getColIdx(i, notzero);
                // mencari solusi dari baris yang bukan 0 semua
                if (idx_col != (-1)) {
                    // inisialisasi Row_Temp untuk nilai sementara di baris yang akan dicari
                    // solusinya
                    if (i == m.getlastRowIdx()) {
                        for (j = 0; j < m.getCol(); j++) {
                            Row_Temp[j] = m.getElmt(i, j);
                        }
                    }

                    else {
                        double under_row = m.getNotZero(i + 1);
                        int idx_under = m.getColIdx(i + 1, under_row);

                        if (idx_under != -1) {
                            for (j = 0; j < m.getCol(); j++) {
                                Row_Temp[j] = m.getElmt(i, j) - m.getElmt(i, idx_under) * Row_Temp[j];
                            }
                        }

                        else {
                            for (j = 0; j < m.getCol(); j++) {
                                Row_Temp[j] = m.getElmt(i, j);
                            }
                        }
                    }

                    // Inisialisasi variabel yang dicari solusinya dengan elemen terakhir di
                    // barisnya
                    Row_Temp[idx_col] = m.getElmt(i, m.getlastColIdx());
                    // Proses meng-assign list Solution dengan eliminasi tiap baris pada Row_Temp
                    if (Row_Temp[idx_col] == 0.0) {
                        int a, ada = 0;
                        for (a = idx_col; a < m.getlastColIdx(); a++) {
                            if (Row_Temp[a] != 0) {
                                ada++;
                            }
                        }
                        if (ada > 0) {
                            Solution[idx_col] = "X" + (idx_col + 1) + " = ";
                        } else {
                            Solution[idx_col] = "X" + (idx_col + 1) + " = " + Row_Temp[idx_col];
                        }
                    } else {
                        Solution[idx_col] = "X" + (idx_col + 1) + " = " + Row_Temp[idx_col];
                    }
                    int k;
                    for (k = idx_col+1; k < m.getlastColIdx(); k++) 
                    {
                        if (isParametrikSolution(m, k))
                        {
                            Solution[k] = "X" + (k+1) + " = " + Parametrik[k-1] + ", dengan " + Parametrik[k-1] + " bilangan Real.";
                            if (Row_Temp[k] != 0)
                            {
                                if (Row_Temp[k] < 0)
                                {
                                    Solution[idx_col] += " + " + (-1)*Row_Temp[k] + " " + Parametrik[k-1];
                                }
                                else
                                {
                                    Solution[idx_col] += " - " + Row_Temp[k] + " " + Parametrik[k-1];
                                }
                            }
                        } else {
                            Row_Temp[idx_col] -= Row_Temp[k];
                        }
                    }
                }
            }
            // OUTPUT
            for (i = 0; i < m.getlastColIdx(); i++) 
            {
                // handling kasus solusi tidak tunggal dalam parametrik yang belum terassign karena index di luar looping
                if (Solution[i] == null) 
                {
                    Solution[i] = "X" + (i+1) + " = " + Parametrik[i] + ", dengan " + Parametrik[i] + " bilangan Real.";
                }
                System.out.println(Solution[i]); // tampilkan ke layar
            }
        }
        // TIdak ada solusi
        else // isSolExist(m) == false
        {
            System.out.println("Sistem persamaan linear tidak memiliki solusi");
        }
    }

    public static void gaussJordanElimination(Matrix m)
    {
        m.getEselonBarisTereduksi();
        
        if (isSolExist(m))
        {
            char[] Parametrik = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'}; // karakter untuk solusi tidak tunggal
            String[] Solution;
            Solution = new String[m.getlastColIdx()];

            int i = 0;
            int idx_col = 0;
            while (idx_col != -1 && i < m.getRow()) 
            {
                double notzero = m.getNotZero(i);
                idx_col = m.getColIdx(i, notzero);

                if (idx_col != -1)
                {
                    if (m.getElmt(i, m.getlastColIdx()) == 0.0)
                    {
                        int a, ada = 0;
                        for (a = idx_col; a < m.getlastColIdx(); a++)
                        {
                            if (m.getElmt(i, a) != 0) 
                            {
                                ada++;
                            }
                        }
                        if (ada > 0)
                        {
                            Solution[idx_col] = "X" + (idx_col+1) + " = ";     
                        }
                        else
                        { 
                            Solution[idx_col] = "X" + (idx_col+1) + " = " + m.getElmt(i, m.getlastColIdx());
                        }       
                    }
                    else
                    {
                        Solution[idx_col] = "X" + (idx_col+1) + " = " + m.getElmt(i, m.getlastColIdx());
                    }
                    int j;
                    for (j = idx_col+1; j < m.getlastColIdx(); j++)
                    {
                        if (m.getElmt(i, j) != 0 && isParametrikSolution(m, j))
                        {
                            Solution[j] = "X" + (j+1) + " = " + Parametrik[j-1] + ", dengan " + Parametrik[j-1] + " bilangan Real.";
                            if (m.getElmt(i, j) < 0)
                            {
                                Solution[idx_col] += " + " + (-1)*m.getElmt(i, j) + " " + Parametrik[j-1];
                            }
                            else
                            {
                                Solution[idx_col] += " - " + m.getElmt(i, j) + " " + Parametrik[j-1];
                            }
                        }
                    }
                }
                i++;
            }
            // OUTPUT
            for (i = 0; i < m.getlastColIdx(); i++) 
            {
                // handling kasus solusi tidak tunggal dalam parametrik yang belum terassign karena index di luar looping
                if (Solution[i] == null) 
                {
                    Solution[i] = "X" + (i+1) + " = " + Parametrik[i] + ", dengan " + Parametrik[i] + " bilangan Real.";
                }
                System.out.println(Solution[i]);    // tampilkan ke layar
            }
        }
        // TIdak ada solusi
        else // isSolExist(m) == false
        {
            System.out.println("Sistem persamaan linear tidak memiliki solusi");
        }
    }
}
