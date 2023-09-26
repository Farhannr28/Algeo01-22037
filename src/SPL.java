package src;

public class SPL {
    public static Matrix cramer(Matrix m) {
        Matrix res, coef;
        res = new Matrix(m.getRow(), 1);
        coef = Matrix.getCoefficient(m);
        double coefDet = Matrix.determinantWithCofactor(coef);
        for (int i = 0; i < m.getCol() - 1; i++) {
            coef = Matrix.getCoefficient(m);
            for (int j = 0; j < m.getRow(); j++) {
                coef.setElmt(j, i, m.getElmt(j, m.col - 1));
            }
            res.setElmt(i, 0, Matrix.determinantWithCofactor(coef) / coefDet);
        }
        res.negatedZero();
        return res;
    }

    public static boolean isSolExist(Matrix m)
    // Mengirim true jika SPL memiliki solusi
    {
        int i;
        boolean solExist = true;
        i = m.getRow() - 1;
        while (solExist == true && i >= 0) {
            if (m.noSolutions(i)) {
                solExist = false;
            }
            i--;
        }
        return solExist;
    }

    public static boolean isParametric(Matrix m) {
        for (int i = 0; i < m.getRow(); i++) {
            if (m.getElmt(i, i) == 0)
                return true;
        }
        return false;
    }

    public static String[] gaussElimination(Matrix m) {
        m.getEselonBaris();
        String[] res = new String[m.getlastColIdx()];
        if (!isSolExist(m)) {
            System.out.println("Tidak ada solusi.");
        } else if (isParametric(m)) {
            int idxParam = 0;
            double[][] paramCnt = new double[m.getlastColIdx()][27];
            boolean[] isVisited = new boolean[m.getlastColIdx()];
            for (int i = 0; i < isVisited.length; i++) {
                isVisited[i] = false;
            }
            for (int i = 0; i < m.getlastColIdx(); i++) {
                for (int j = 0; j < 27; j++) {
                    paramCnt[i][j] = 0;
                }
            }
            for (int i = m.getlastRowIdx(); i >= 0; i--) {
                int idx = m.getColIdx(i, m.getNotZero(i)); // index dengan element tidak zero pertama
                if (idx != -1) {
                    paramCnt[idx][0] = m.getElmt(i, m.getlastColIdx());
                    for (int j = idx + 1; j < m.getlastColIdx(); j++) {
                        if (m.getElmt(i, j) == 0)
                            continue;
                        if (isVisited[j]) {
                            for (int k = 0; k < 27; k++) {
                                paramCnt[idx][k] -= (paramCnt[j][k] * m.getElmt(i, j));
                            }
                        } else {
                            idxParam++;
                            paramCnt[j][idxParam] = 1;
                            paramCnt[idx][idxParam] -= (m.getElmt(i, j) * paramCnt[j][idxParam]);
                            isVisited[j] = true;
                        }
                    }
                    isVisited[idx] = true;
                }
            }
            for (int i = 0; i < m.getlastColIdx(); i++) {
                if (!isVisited[i]) {
                    idxParam++;
                    paramCnt[i][idxParam] = 1;
                    isVisited[i] = true;
                }
            }
            for (int i = 0; i < m.getlastColIdx(); i++) {
                String temp = "";
                boolean first = false;
                for (int k = 1; k < 27; k++) {
                    if (paramCnt[i][k] != 0) {
                        if (paramCnt[i][k] < 0) {
                            if (!first) {
                                if (paramCnt[i][k] != -1)
                                    temp += paramCnt[i][k];
                                else
                                    temp += "-";
                            } else if (paramCnt[i][k] != -1)
                                temp += "- " + paramCnt[i][k];
                            else
                                temp += "- ";

                            temp += ((char) (k + 96));
                            temp += " ";
                            first = true;
                        } else {
                            if (first)
                                temp += "+ ";
                            if (paramCnt[i][k] != 1)
                                temp += paramCnt[i][k];
                            temp += ((char) (k + 96));
                            temp += " ";
                            first = true;
                        }
                    }
                }
                if (!first) {
                    temp += paramCnt[i][0];
                } else if (paramCnt[i][0] < 0) {
                    temp += "- " + (-paramCnt[i][0]);
                } else if (paramCnt[i][0] > 0) {
                    temp += "+ " + paramCnt[i][0];
                }
                res[i] = temp;
            }
        } else {
            double[] doubleRes = new double[m.getlastColIdx()];
            for (int i = m.getlastRowIdx(); i >= 0; i--) {
                double tempRes = m.getElmt(i, m.getlastColIdx());
                for (int j = i + 1; j < m.getlastColIdx(); j++) {
                    tempRes -= (m.getElmt(i, j) * doubleRes[j]);
                }
                doubleRes[i] = tempRes;
            }
            for (int i = 0; i < m.getlastColIdx(); i++) {
                res[i] = Double.toString(doubleRes[i]);
            }
        }
        return res;
    }

    public static String[] gaussJordanElimination(Matrix m) {
        m.getEselonBarisTereduksi();
        String[] res = new String[m.getlastColIdx()];
        if (!isSolExist(m)) {
            System.out.println("Tidak ada solusi.");
        } else if (isParametric(m)) {
            int idxParam = 0;
            double[][] paramCnt = new double[m.getlastColIdx()][27];
            boolean[] isVisited = new boolean[m.getlastColIdx()];
            for (int i = 0; i < isVisited.length; i++) {
                isVisited[i] = false;
            }
            for (int i = 0; i < m.getlastColIdx(); i++) {
                for (int j = 0; j < 27; j++) {
                    paramCnt[i][j] = 0;
                }
            }
            for (int i = m.getlastRowIdx(); i >= 0; i--) {
                int idx = m.getColIdx(i, m.getNotZero(i)); // index dengan element tidak zero pertama
                if (idx != -1) {
                    paramCnt[idx][0] = m.getElmt(i, m.getlastColIdx());
                    for (int j = idx + 1; j < m.getlastColIdx(); j++) {
                        if (m.getElmt(i, j) == 0)
                            continue;
                        if (isVisited[j]) {
                            for (int k = 0; k < 27; k++) {
                                paramCnt[idx][k] -= (paramCnt[j][k] * m.getElmt(i, j));
                            }
                        } else {
                            idxParam++;
                            paramCnt[j][idxParam] = 1;
                            paramCnt[idx][idxParam] -= (m.getElmt(i, j) * paramCnt[j][idxParam]);
                            isVisited[j] = true;
                        }
                    }
                    isVisited[idx] = true;
                }
            }
            for (int i = 0; i < m.getlastColIdx(); i++) {
                if (!isVisited[i]) {
                    idxParam++;
                    paramCnt[i][idxParam] = 1;
                    isVisited[i] = true;
                }
            }
            for (int i = 0; i < m.getlastColIdx(); i++) {
                String temp = "";
                boolean first = false;
                for (int k = 1; k < 27; k++) {
                    if (paramCnt[i][k] != 0) {
                        if (paramCnt[i][k] < 0) {
                            if (!first) {
                                if (paramCnt[i][k] != -1)
                                    temp += paramCnt[i][k];
                                else
                                    temp += "-";
                            } else if (paramCnt[i][k] != -1)
                                temp += "- " + paramCnt[i][k];
                            else
                                temp += "- ";

                            temp += ((char) (k + 96));
                            temp += " ";
                            first = true;
                        } else {
                            if (first)
                                temp += "+ ";
                            if (paramCnt[i][k] != 1)
                                temp += paramCnt[i][k];
                            temp += ((char) (k + 96));
                            temp += " ";
                            first = true;
                        }
                    }
                }
                if (!first) {
                    temp += paramCnt[i][0];
                } else if (paramCnt[i][0] < 0) {
                    temp += "- " + (-paramCnt[i][0]);
                } else if (paramCnt[i][0] > 0) {
                    temp += "+ " + paramCnt[i][0];
                }
                res[i] = temp;
            }
        } else {
            for (int i = m.getlastRowIdx(); i >= 0; i--) {
                res[i] = Double.toString(m.getElmt(i, m.getlastColIdx()));
            }
        }
        return res;
    }
}
