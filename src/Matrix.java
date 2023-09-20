package src;

import java.util.*;

public class Matrix{
    // STRUKTUR DATA //
    double [][] Mat;
    int row, col;
    static Scanner input = new Scanner(System.in);


    // CONSTRUCTOR // 
    Matrix(int _row, int _col){
        this.row = _row;
        this.col = _col;
        this.Mat = new double[_row][_col];
    }

    // SELECTOR //
    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    public double getElmt(int i, int j){
        return this.Mat[i][j];
    }

    public int getLength(){
        return getRow() * getCol();
    }

    public int getColIdx(int row, double x)
    // Mengirim nilai index kolom di mana elemen ditemukan di suatu baris
    // mengirim index -1 bila tidak ditemukan
    {
        int i;
        for (i = 0; i < getCol(); i++)
        {
            if (getElmt(row, i) == x)
            {
                return i;
            }
        }
        return (-1);
    }

    // INPUT & OUTPUT //
    public void readMatrix(){
        for (int i = 0; i < getRow(); i++){
            for (int j = 0; j < getCol(); j++){
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
    public void addWith(Matrix M2){
        for (int i = 0; i < getRow(); i++){
            for (int j = 0; j < getCol(); j++){
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
        for (i = 0; i < getCol() - 1; i++) 
        {
            if (getElmt(row, i) != 0) 
            {
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
        for (i = 0; i < getCol(); i++) 
        {
            if (getElmt(row, i) == 0) 
            {
                sum++;
            }
            else
            {
                return sum;
            }
        }
        return sum;
    }

    public void swap(int row1, int row2)
    // Menukarkan baris row1 dengan baris row2, dan sebaliknya
    {
        double temp;    // tempat penyimpanan elemen sementara
        int i;
        for (i = 0; i < getCol(); i++)
        {
            temp = getElmt(row1, i);
            this.Mat[row1][i] = this.Mat[row2][i];
            this.Mat[row2][i] = temp;
        }
    }
    
    public double getNotZero(int row)
    // Mendapatkan angka pertama bukan nol pada suatu baris.
    // ex: 0 0 9 0 3 0 --> menghasilkan output 9 (angka pertama bukan 0).
    // Jika tidak ada (0 semua), maka mengembalikan nilai 1 (agar bisa digunakan sebagai dividen)
    {
        int i;
        for (i = 0; i < getCol(); i++) 
        {
            if (getElmt(row, i) != 0) 
            {
                return getElmt(row, i);
            }
        }
        return 1;
    }

    
    public void dividedRow(int row, double x)
    // Membagi setiap elemen pada baris dengan suatu konstanta
    {
        int i;
        for (i = 0; i < getCol(); i++) 
        {
            this.Mat[row][i] = this.Mat[row][i] / x;
        }
    }

    /* BELUM KEPAKE TERNYATA GA JADI MAKE
    public void multiplyRow(int row, double x)
    // Mengalikan setiap elemen pada baris dengan suatu konstanta
    {
        int i;
        for (i = 0; i < getCol(); i++) 
        {
            this.Mat[row][i] = this.Mat[row][i] * x;
        }        
    }
    
    public void minusRow(int row, double x)
    // Mengurangi setiap elemen pada baris dengan suatu konstanta 
    {
        int i;
        for (i = 0; i < getCol(); i++) 
        {
            this.Mat[row][i] = this.Mat[row][i] - x;
        }         
    }*/
    
    public void sortRowZero()
    // Mengurutkan baris berdasarkan jumlah angka 0 di depan, terurut membesar
    // Jika jumlah angka 0 di depan sama, maka baris dengan angka 1 pertama kali setelah 0 (leading 1) didahulukan
    {
        boolean isSwap = true;
        int i, row = 0;

        while (row < getRow() - 1 && isSwap)
        {
            isSwap = false;
            for (i = getRow() - 1; i > row; i--)
            {
                if ((countZero(i) < countZero(i - 1)) || (countZero(i) == countZero(i - 1) && getNotZero(i) == 1 && getNotZero(i - 1) != 1))
                {
                    swap(i, i - 1);
                    isSwap = true;
                }
            }
            row++;
        }
    }

    public void getEselonBaris()
    // Mendapatkan matrix eselon baris
    {
        sortRowZero();  // mengurutkan baris berdasarkan jumlah angka 0 di depan

        int i, j, k, idx_col;
        double nZero, underlead;
        for (i = 0; i < getRow(); i++)
        {
            nZero = getNotZero(i);
            dividedRow(i, nZero);   // membagi elemen dalam satu baris dengan angka bukan 0 terdepan agar menjadi leading one
            idx_col = getColIdx(i, 1);
            
            // cek di bawah leading one tsb apakah ada yang != 0
            j = i + 1;
            while (j < getRow())
            {
                // kondisi di bawah leading one tsb ada yang!= 0
                if (getElmt(j, idx_col) != 0) 
                {   
                    // melakukan operasi baris elementer agar semua ELMT di bawah leading one tsb = 0
                    underlead = getElmt(j, idx_col);
                    for (k = 0; k < getCol(); k++)
                    {
                        this.Mat[j][k] = getElmt(j, k) - (underlead * getElmt(i, k));
                    }
                }
                j++;
            }
        }
    }

    public void getEselonBaris_Tereduksi()
    // Mendapatkan matrix eselon baris tereduksi
    {
        getEselonBaris();   // dapatkan eselon baris

        int i, j, k, idx_col;
        double above;
        for (i = getRow() - 1; i > 0; i--)
        {
            idx_col = getColIdx(i, 1);
            if (idx_col != -1)
            {
                // cek di atas leading one tsb apakah ada yang != 0
                for (j = 0; j < i; j++)
                {
                    above = getElmt(j, idx_col);
                    if (above != 0) // kondisi di atas leading one tsb ada yang != 0
                    {
                        // melakukan operasi baris elementer agar semua ELMT di atas leading one tsb = 0
                        for (k = getCol() - 1; k >= idx_col; k--)  
                        {
                            this.Mat[j][k] = getElmt(j, k) - (above * getElmt(i, k));
                        }  
                    }
                }
            }
        }
    }
}
