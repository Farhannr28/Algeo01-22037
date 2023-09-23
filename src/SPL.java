package src;

public class SPL {
    public static Matrix cramer(Matrix m){
        Matrix res, coef;
        res = new Matrix(m.row, 1);
        coef = Matrix.getCoefficient(m);
        double coefDet = Matrix.determinantWithCofactor(coef);
        for (int i = 0; i < m.col - 1; i++){
            coef = Matrix.getCoefficient(m);
            for (int j = 0; j < m.row; j++){
                coef.Mat[j][i] = m.Mat[j][m.col - 1];
            }
            res.Mat[i][0] = Matrix.determinantWithCofactor(coef)/coefDet;
        }
        res.negatedZero();
        return res;
    }
}
