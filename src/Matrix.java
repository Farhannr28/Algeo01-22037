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

    // INPUT & OUTPUT //
    public void readMatrix(){
        for (int i = 0; i < getRow(); i++){
            for (int j = 0; j < getCol(); j++){
                this.Mat[i][j] = input.nextDouble();
            }
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
}