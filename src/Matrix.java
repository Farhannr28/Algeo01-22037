package src;

import java.util.*;

public class Matrix {
    // STRUKTUR DATA //
    double[][] Mat;
    int row, col;
    static Scanner input = new Scanner(System.in);

    // CONSTRUCTOR //
    Matrix(int _row, int _col) {
        this.row = _row;
        this.col = _col;
        this.Mat = new double[_row][_col];
    }

    // SELECTOR //
    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public double getElmt(int i, int j) {
        return this.Mat[i][j];
    }

    public int getLength() {
        return getRow() * getCol();
    }

    public int getColIdx(int row, double x)
    // Mengirim nilai index kolom di mana elemen ditemukan di suatu baris
    // mengirim index -1 bila tidak ditemukan
    {
        int i;
        for (i = 0; i < getCol(); i++) {
            if (getElmt(row, i) == x) {
                return i;
            }
        }
        return (-1);
    }

    public int getlastRowIdx()
    // Mengirim nilai index baris terakhir matrix
    {
        return this.getRow() - 1;
    }

    public int getlastColIdx()
    // Mengirim nilai index kolom terakhir matrix
    {
        return this.getCol() - 1;
    }

    public void setElmt(int i, int j, double val) {
        this.Mat[i][j] = val;
    }

    // INPUT & OUTPUT //
    public void readMatrix() {
        for (int i = 0; i < getRow(); i++) {
            for (int j = 0; j < getCol(); j++) {
                this.Mat[i][j] = input.nextDouble();
            }
        }
    }

    public void displayMatrix()
    // Menampilkan matrix
    {
        int i, j;
        for (i = 0; i < getRow(); i++) {
            System.out.print(getElmt(i, 0));
            for (j = 1; j < getCol(); j++) {
                System.out.print(" " + getElmt(i, j));
            }
            System.out.println("");
        }
    }

    // OPERATIONS //
    public void addWith(Matrix M2) {
        for (int i = 0; i < getRow(); i++) {
            for (int j = 0; j < getCol(); j++) {
                this.Mat[i][j] += M2.Mat[i][j];
            }
        }
    }

    public boolean noSolutions(int row)
    // Mengirim True jika matrix tidak menghasilkan solusi
    // Memiliki pola yaitu salah satu barisnya 0 0 0 x, dengan x angka sembarang
    // ex: pada matrix 5x5 terdapat baris 0 0 0 0 4
    {
        int i;
        for (i = 0; i < getCol() - 1; i++) {
            if (getElmt(row, i) != 0) {
                return false;
            }
        }
        return (getElmt(row, i) != 0);
    }

    public int countZero(int row)
    // Mencari ada berapa jumlah angka 0 di depan angka bukan 0 di suatu baris
    // ex: 0 0 2 0 0 3 --> menghasilkan output 2 (angka 0 di depan ada 2)
    {
        int i, sum = 0;
        for (i = 0; i < getCol(); i++) {
            if (getElmt(row, i) == 0) {
                sum++;
            } else {
                return sum;
            }
        }
        return sum;
    }

    public void swap(int row1, int row2)
    // Menukarkan baris row1 dengan baris row2, dan sebaliknya
    {
        double temp; // tempat penyimpanan elemen sementara
        int i;
        for (i = 0; i < getCol(); i++) {
            temp = getElmt(row1, i);
            this.Mat[row1][i] = this.Mat[row2][i];
            this.Mat[row2][i] = temp;
        }
    }

    public double getNotZero(int row)
    // Mendapatkan angka pertama bukan nol pada suatu baris.
    // ex: 0 0 9 0 3 0 --> menghasilkan output 9 (angka pertama bukan 0).
    // Jika tidak ada (0 semua), maka mengembalikan nilai 1 (agar bisa digunakan
    // sebagai dividen)
    {
        int i;
        for (i = 0; i < getCol(); i++) {
            if (getElmt(row, i) != 0) {
                return getElmt(row, i);
            }
        }
        return 1;
    }

    public void dividedRow(int row, double x)
    // Membagi setiap elemen pada baris dengan suatu konstanta
    {
        int i;
        for (i = 0; i < getCol(); i++) {
            this.Mat[row][i] = this.Mat[row][i] / x;
        }
    }

    public void negatedZero()
    // Mengatasi nilai negatif 0 yang terdapat pada matrix eselon
    {
        int i, j;
        for (i = 0; i < getRow(); i++) {
            for (j = 0; j < getCol(); j++) {
                if (getElmt(i, j) == -0.0) {
                    this.Mat[i][j] = 0;
                }
            }
        }
    }

    public int sortRowZero()
    // Mengurutkan baris berdasarkan jumlah angka 0 di depan, terurut membesar
    // Jika jumlah angka 0 di depan sama, maka baris dengan angka 1 pertama kali
    // setelah 0 (leading 1) didahulukan
    {
        boolean isSwap = true;
        int i, row = 0, cnt = 0;

        while (row < getRow() - 1 && isSwap) {
            isSwap = false;
            for (i = getRow() - 1; i > row; i--) {
                if ((countZero(i) < countZero(i - 1))
                        || (countZero(i) == countZero(i - 1) && getNotZero(i) == 1 && getNotZero(i - 1) != 1)) {
                    swap(i, i - 1);
                    cnt++;
                    isSwap = true;
                }
            }
            row++;
        }
        return cnt;
    }

    public void getEselonBaris()
    // Mendapatkan matrix eselon baris
    {
        sortRowZero(); // mengurutkan baris berdasarkan jumlah angka 0 di depan

        int i, j, k, idx_col;
        double nZero, underlead;
        for (i = 0; i < getRow(); i++) {
            nZero = getNotZero(i);
            dividedRow(i, nZero); // membagi elemen dalam satu baris dengan angka bukan 0 terdepan agar menjadi
                                  // leading one
            idx_col = getColIdx(i, 1);

            // cek di bawah leading one tsb apakah ada yang != 0
            j = i + 1;
            while (j < getRow() && idx_col != (-1)) {
                // kondisi di bawah leading one tsb ada yang!= 0
                if (getElmt(j, idx_col) != 0) {
                    // melakukan operasi baris elementer agar semua ELMT di bawah leading one tsb =
                    // 0
                    underlead = getElmt(j, idx_col);
                    for (k = idx_col; k < getCol(); k++) {
                        this.Mat[j][k] = getElmt(j, k) - (underlead * getElmt(i, k));
                    }
                }
                j++;
            }
            sortRowZero(); // mengurutkan baris berdasarkan jumlah angka 0 di depan
        }
        negatedZero(); // mengatasi negatif 0 pada matrix eselon
    }

    public void getEselonBarisTereduksi()
    // Mendapatkan matrix eselon baris tereduksi
    {
        getEselonBaris(); // dapatkan eselon baris

        int i, j, k, idx_col;
        double above;
        for (i = getlastRowIdx(); i > 0; i--) {
            idx_col = getColIdx(i, 1);
            if (idx_col != -1) {
                // cek di atas leading one tsb apakah ada yang != 0
                for (j = 0; j < i; j++) {
                    above = getElmt(j, idx_col);
                    if (above != 0) // kondisi di atas leading one tsb ada yang != 0
                    {
                        // melakukan operasi baris elementer agar semua ELMT di atas leading one tsb = 0
                        for (k = getCol() - 1; k >= idx_col; k--) {
                            this.Mat[j][k] = getElmt(j, k) - (above * getElmt(i, k));
                        }
                    }
                }
            }
        }
        negatedZero(); // mengatasi negatif 0 pada matrix eselon
    }

    public static Matrix getCoefficient(Matrix m) {
        Matrix res = new Matrix(m.row, m.col - 1);
        for (int i = 0; i < res.row; i++) {
            for (int j = 0; j < res.col; j++) {
                res.Mat[i][j] = m.Mat[i][j];
            }
        }
        return res;
    }

    public static Matrix minor(Matrix m, int r, int c) {
        Matrix minorM = new Matrix(m.row - 1, m.col - 1);
        int rm = 0, cm = 0;
        for (int i = 0; i < m.row; i++) {
            if (i != r) {
                cm = 0;
                for (int j = 0; j < m.col; j++) {
                    if (j != c) {
                        minorM.Mat[rm][cm] = m.Mat[i][j];
                        cm++;
                    }
                }
                rm++;
            }
        }
        return minorM;
    }

    public static double determinantWithCofactor(Matrix m1) {
        if (m1.row == 1) {
            return m1.Mat[0][0];
        }
        double res = 0;
        for (int i = 0; i < m1.row; i++) {
            res += (i % 2 == 1) ? (-1.0) * m1.Mat[0][i] * determinantWithCofactor(minor(m1, 0, i))
                    : m1.Mat[0][i] * determinantWithCofactor(minor(m1, 0, i));
        }
        if (res == -0.0){
            return 0;
        }
        return res;
    }

    public int findIdxNotZero(int r) {
        for (int i = 0; i < this.col; i++) {
            if (this.Mat[r][i] != 0)
                return i;
        }
        return -1;
    }

    public static double determinantWithOBE(Matrix m1) {
        int cnt = 0;
        double res = 1;
        cnt += m1.sortRowZero();
        System.out.println();
        for (int i = 0; i < m1.row; i++) {
            int firstNonZeroIdx = m1.findIdxNotZero(i);
            for (int j = i + 1; j < m1.row; j++) {
                if (m1.Mat[j][firstNonZeroIdx] != 0) {
                    double mult = m1.Mat[j][firstNonZeroIdx];
                    for (int k = firstNonZeroIdx; k < m1.col; k++) {
                        m1.Mat[j][k] = m1.Mat[j][k] - (m1.Mat[i][k] / m1.Mat[i][firstNonZeroIdx]) * mult;
                    }
                }
            }
            cnt += m1.sortRowZero();
        }
        m1.negatedZero();
        for (int i = 0; i < m1.row; i++) {
            res *= m1.Mat[i][i];
        }
        if (res == -0.0 || res == 0){
            return 0;
        }
        if (cnt % 2 == 0){
            return res;
        } else {
            return -res;
        }
    }

    public void copyMatrix(Matrix M){
    // Menduplikasi nilai matrikx dari M
    // Prequisite : ukuran kedua matrix sama
        for(int i=0; i<getRow(); i++){
            for (int j=0; j<getCol(); j++){
                this.Mat[i][j] = M.Mat[i][j];
            }
        }
    }

    public static Matrix transpose(Matrix m){
    // Mentranspose Matrix
        Matrix temp = new Matrix(m.getCol(), m.getRow());
        for (int i=0; i<m.getCol(); i++){
            for (int j=0; j<m.getRow(); j++){
                temp.Mat[i][j] = m.getElmt(j,i);
            }
        }
        return temp;
    }

    public static Matrix multiplyMatrix(Matrix M1, Matrix M2){
    // Mengeluarkan Matrix MxN yang merupakan hasil perkalian Matrix MxK dengan KxN
        Matrix ans = new Matrix(M1.getRow(), M2.getCol());
        double temp;
        for (int i=0; i<M1.getRow(); i++){
            for (int j=0; j<M2.getCol(); j++){
                temp = 0;
                for (int k=0; k<M1.getCol(); k++){
                    temp = temp + (M1.Mat[i][k] * M2.Mat[k][j]);
                }
                ans.Mat[i][j] = temp;
            }
        }
        return ans;
    }

    public boolean isIdentity(){
        boolean identity = true;
        for (int i=0; i<getRow(); i++){
            for (int j=0; j<getCol(); j++){
                if (i==j){
                    if (getElmt(i, j)!=1){
                        identity = false;
                    }
                } else {
                    if (getElmt(i, j)!=0){
                        identity = false;
                    }
                }
            }
        }
        return identity;
    }

    public static Matrix getIdentityMatrix(int n){
    // Mengirimkan Matrix identitas dari Matrix ukuran NxN
        Matrix identity = new Matrix(n, n);
        for (int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                if (i==j){
                    identity.Mat[i][j] = 1;
                } else {
                    identity.Mat[i][j] = 0;
                }
            }
        }    
        return identity;
    }
    
    public Matrix sortRowZeroInvers(Matrix m){
        boolean isSwap = true;
        int i, row = 0;
        while (row < getRow() - 1 && isSwap) {
            isSwap = false;
            for (i = getRow() - 1; i > row; i--) {
                if ((countZero(i) < countZero(i - 1))
                        || (countZero(i) == countZero(i - 1) && getNotZero(i) == 1 && getNotZero(i - 1) != 1)) {
                    swap(i, i - 1);
                    m.swap(i, i-1);
                    isSwap = true;
                }
            }
            row++;
        }
        return m;
    }

    public Matrix getEselonTereduksiInvers(Matrix m){
        // Gauss
        m = this.sortRowZeroInvers(m); // mengurutkan baris berdasarkan jumlah angka 0 di depan
        int i, j, k, idx_col;
        double nZero, underlead;
        for (i = 0; i < getRow(); i++) {
            nZero = getNotZero(i);
            dividedRow(i, nZero); // membagi elemen dalam satu baris dengan angka bukan 0 terdepan agar menjadi leading one
            m.dividedRow(i, nZero);
            idx_col = getColIdx(i, 1);

            // cek di bawah leading one tsb apakah ada yang != 0
            j = i + 1;
            while (j < getRow() && idx_col != (-1)) {
                // kondisi di bawah leading one tsb ada yang!= 0
                if (getElmt(j, idx_col) != 0) {
                    // melakukan operasi baris elementer agar semua ELMT di bawah leading one tsb = 0
                    underlead = getElmt(j, idx_col);
                    for (k = 0; k < getCol(); k++) {
                        this.Mat[j][k] = getElmt(j, k) - (underlead * getElmt(i, k));
                        m.Mat[j][k] -= (underlead * m.Mat[i][k]);
                    }
                }
                j++;
            }
            m = this.sortRowZeroInvers(m); // mengurutkan baris berdasarkan jumlah angka 0 di depan
            negatedZero(); // mengatasi negatif 0 pada matrix eselon
            m.negatedZero();
        }
        // Gauss-Jordan
        double above;
        for (i = getlastRowIdx(); i > 0; i--) {
            idx_col = getColIdx(i, 1);
            if (idx_col != -1) {
                // cek di atas leading one tsb apakah ada yang != 0
                for (j = 0; j < i; j++) {
                    above = getElmt(j, idx_col);
                    if (above != 0) // kondisi di atas leading one tsb ada yang != 0
                    {
                        // melakukan operasi baris elementer agar semua ELMT di atas leading one tsb = 0
                        for (k = getCol() - 1; k >= 0; k--) {
                            this.Mat[j][k] = getElmt(j, k) - (above * getElmt(i, k));
                            m.Mat[j][k] -= (above * m.Mat[i][k]);
                        }
                    }
                }
            }
            negatedZero(); // mengatasi negatif 0 pada matrix eselon
            m.negatedZero();
        }
        return m;
    }

    public boolean InversWithGaussJordan(){
        // Mengembalikan True jika Invers berhasil
        Matrix Invers = new Matrix(getRow(), getCol());
        Invers.copyMatrix(this);
        if (determinantWithOBE(Invers)==0.0){
            // Gagal di invers karena determinannya 0
            return false;
        }
        if (Invers.isIdentity()){
            // Invers dari identitas adalah dirinya sendiri
            return true;
        }
        Matrix Identity = getIdentityMatrix(this.getRow());
        Invers = this.getEselonTereduksiInvers(Identity);
        this.copyMatrix(Invers);
        return true; 
    }

    public static boolean InversWithCofactor(Matrix m){
        // Mengembalikan True jika Invers berhasil
        Matrix Invers = new Matrix(m.getRow(), m.getCol());
        Invers.copyMatrix(m);
        double det = determinantWithCofactor(Invers);
        if (det==0.0){
            // Gagal di invers karena determinannya 0
            return false;
        }
        if (Invers.isIdentity()){
            // Invers dari identitas adalah dirinya sendiri
            return true;
        }
        for (int i=0; i<m.getRow(); i++){
            for (int j=0; j<m.getCol(); j++){
                Invers.Mat[i][j] = determinantWithCofactor(minor(m,i,j));
                if ((i+j) % 2 != 0){
                    Invers.Mat[i][j] = 0 - Invers.Mat[i][j];
                }
            }
        }
        Matrix tr = new Matrix(m.getRow(), m.getCol());
        tr = transpose(Invers);
        for (int i=0; i<m.getRow(); i++){
            for (int j=0; j<m.getCol(); j++){
                tr.Mat[i][j] /= det;
            }
        }
        tr.negatedZero();
        m.copyMatrix(tr);
        return true; 
    }
}