package src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageInterpolation {
    public static void main(String[] args) throws IOException {
        // Declare
        Matrix X = new Matrix(16, 16);
        Matrix D = new Matrix(16, 16);
        Matrix inverseX = new Matrix(16, 16);
        Matrix multiplier = new Matrix(16, 16);

        // Construct X and D
        InterpolasiBikubik.constructX(X);
        ImageInterpolation.constructD(D);
        X.displayMatrix();
        System.out.println();
        D.displayMatrix();
        System.out.println();
        // inverse matrix and save it to inverseX
        inverseX.copyMatrix(X);
        inverseX.InversWithGaussJordan();

        // multiply X and D and save it to multiplier
        multiplier = Matrix.multiplyMatrix(inverseX, D);
        // double[][] makan = {
        //     {0,0,0,0,0,16,0,0,0,0,0,0,0,0,0,0},
        //     {0,0,0,0,-8,0,8,0,0,0,0,0,0,0,0,0},
        //     {0,0,0,0,16,-40,32,-8,0,0,0,0,0,0,0,0},
        //     {0,0,0,0,-8,24,-24,8,0,0,0,0,0,0,0,0},
        //     {0,-8,0,0,0,0,0,0,0,8,0,0,0,0,0,0},
        //     {0,-4,0,0,-4,4,0,0,0,0,4,0,0,0,0,0},
        //     {0,32,-20,0,8,-4,-4,0,0,-24,16,-4,0,0,0,0},
        //     {0,-20,12,0,-4,0,4,0,0,16,-12,4,0,0,0,0},
        //     {0,16,0,0,0,-40,0,0,0,32,0,0,0,-8,0,0},
        //     {0,8,0,0,32,-4,-24,0,-20,-4,0,0,0,0,-4,0},
        //     {0,-64,40,0,-64,96,-68,24,40,-68,-16,-16,0,24,-16,0},
        //     {0,40,-24,0,32,-52,52,-24,-20,40,16,16,0,-16,12,0},
        //     {0,-8,0,0,0,24,0,0,0,-24,0,0,0,8,0,4},
        //     {0,-4,0,0,-20,0,16,0,12,4,-12,0,0,0,4,-4},
        //     {0,32,-20,0,40,-52,40,-16,-24,52,-52,12,0,-24,16,-4},
        //     {0,-20,12,0,-20,28,-32,16,12,-32,40,-12,0,16,-12,4}
        // };
        for (int i = 0; i < multiplier.getRow(); i++){
            for (int j = 0; j < multiplier.getCol(); j++){
                multiplier.setElmt(i, j, multiplier.getElmt(i, j) / 4);
            }
        }
        multiplier.displayMatrix();

        // get image file
        String fileName = InterpolasiBikubik.readileName();
        File file = new File(fileName);
        BufferedImage img = ImageIO.read(file);

        Matrix red = new Matrix(img.getHeight(), img.getWidth());
        Matrix blue = new Matrix(img.getHeight(), img.getWidth());
        Matrix green = new Matrix(img.getHeight(), img.getWidth());
        Matrix tempRed = new Matrix(16, 1);
        Matrix tempBlue = new Matrix(16, 1);
        Matrix tempGreen = new Matrix(16, 1);

        Matrix[][] redRes = new Matrix[img.getHeight() - 3][img.getWidth() - 3];
        Matrix[][] blueRes = new Matrix[img.getHeight() - 3][img.getWidth() - 3];
        Matrix[][] greenRes = new Matrix[img.getHeight() - 3][img.getWidth() - 3];

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                int pixel = img.getRGB(j, i);
                Color color = new Color(pixel, true);
                red.setElmt(i, j, color.getRed());
                blue.setElmt(i, j, color.getBlue());
                green.setElmt(i, j, color.getGreen());
            }
        }
        for (int i = 1; i <= img.getHeight() - 3; i++) {
            for (int j = 1; j <= img.getWidth() - 3; j++) {
                int idx = 0;
                for (int y = j - 1; y < j + 3; y++) {
                    for (int x = i - 1; x < i + 3; x++) {
                        tempRed.setElmt(idx, 0, red.getElmt(x, y));
                        tempGreen.setElmt(idx, 0, green.getElmt(x, y));
                        tempBlue.setElmt(idx, 0, blue.getElmt(x, y));
                        idx++;
                    }
                }
                redRes[i - 1][j - 1] = Matrix.multiplyMatrix(multiplier, tempRed);
                greenRes[i - 1][j - 1] = Matrix.multiplyMatrix(multiplier, tempGreen);
                blueRes[i - 1][j - 1] = Matrix.multiplyMatrix(multiplier, tempBlue);
            }
        }

        System.out.print("Ingin memberbesar berapa: ");
        double k = 2;

        long newHeight = Math.round(k * img.getHeight());
        long newWidth = Math.round(k * img.getWidth());
        BufferedImage newImage = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                double conX = ((double) img.getHeight() / (double) newHeight) * ((double) (2 * i + 1) / (double) 2)
                        - 0.5;
                double conY = ((double) img.getWidth() / (double) newWidth) * ((double) (2 * j + 1) / (double) 2) - 0.5;
                if (1 <= conX && conX <= (double) img.getHeight() - 3 && 1 <= conY
                        && conY <= (double) img.getWidth() - 3) {
                    int x, y, rgb = 0;
                    // if (conX != (double) img.getHeight() - 2) {
                    //     x = (int) Math.floor(conX) - 1;
                    //     conX = conX - Math.floor(conX);
                    // } else {
                    //     x = img.getHeight() - 4;
                    //     conX = 1;
                    // }
                    // if (conY != (double) img.getWidth() - 2) {
                    //     y = (int) Math.floor(conY) - 1;
                    //     conY = conY - Math.floor(conY);
                    // } else {
                    //     y = img.getWidth() - 4;
                    //     conY = 1;
                    // }
                    x = (int) Math.floor(conX);
                    x -= 1;
                    conX -= Math.floor(conX);
                    y = (int) Math.floor(conY);
                    y -= 1;
                    conY -= Math.floor(conY);
                    double r = calcRGB(redRes[x][y], conX, conY);
                    double g = calcRGB(greenRes[x][y], conX, conY);
                    double b = calcRGB(blueRes[x][y], conX, conY);
                    rgb = 0;
                    if (r >= 0)
                        rgb += (int) Math.round(r);
                    rgb <<= 8;
                    if (g >= 0)
                        rgb += (int) Math.round(g);
                    rgb <<= 8;
                    if (b >= 0)
                        rgb += (int) Math.round(b);
                    newImage.setRGB(j, i, rgb);
                } else {
                    int x, y, rgb;
                    if (conX < 0) {
                        x = 0;
                    } else {
                        x = (int) Math.round(conX);
                    }
                    if (conY < 0) {
                        y = 0;
                    } else {
                        y = (int) Math.round(conY);
                    }
                    rgb = (int) red.getElmt(x, y);
                    rgb = (rgb << 8) + (int) green.getElmt(x, y);
                    rgb = (rgb << 8) + (int) blue.getElmt(x, y);
                    newImage.setRGB(j, i, rgb);
                }
            }
        }
        String outputFileName = "C:\\Users\\adril\\ITB Files\\Semester III\\Linear and Geometry Algebra\\Tubes\\Tubes 1\\Algeo01-22037\\test\\output.png";
        File outputImg = new File(outputFileName);
        ImageIO.write(newImage, "png", outputImg);
    }

    public static double calcRGB(Matrix mult, double x, double y) {
        double res = 0;
        int idx = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                res += (mult.getElmt(idx, 0) * Math.pow(x, i) * Math.pow(y, j));
                idx++;
            }
        }
        return res;
    }

    public static void constructD(Matrix D) {
        int row = 0, col = 0;
        // f(x,y)
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                col = 0;
                for (int j = -1; j < 3; j++) {
                    for (int i = -1; i < 3; i++) {
                        if (x == i && y == j)
                            D.setElmt(row, col, 4);
                        else
                            D.setElmt(row, col, 0);
                        col++;
                    }
                }
                row++;
            }
        }
        // fx(x, y)
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                col = 0;
                for (int j = -1; j < 3; j++) {
                    for (int i = -1; i < 3; i++) {
                        if (x + 1 == i && y == j)
                            D.setElmt(row, col, 2);
                        else if (x - 1 == i && y == j)
                            D.setElmt(row, col, -2);
                        else
                            D.setElmt(row, col, 0);
                        col++;
                    }
                }
                row++;
            }
        }
        // fy(x, y)
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                col = 0;
                for (int j = -1; j < 3; j++) {
                    for (int i = -1; i < 3; i++) {
                        if (x == i && y + 1 == j)
                            D.setElmt(row, col, 2);
                        else if (x == i && y - 1 == j)
                            D.setElmt(row, col, -2);
                        else
                            D.setElmt(row, col, 0);
                        col++;
                    }
                }
                row++;
            }
        }
        // fxy(x, y)
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                col = 0;
                for (int j = -1; j < 3; j++) {
                    for (int i = -1; i < 3; i++) {
                        if ((x + 1 == i && y + 1 == j) || (x == i && y == j))
                            D.setElmt(row, col, 1);
                        else if ((x - 1 == i && y == j) || (x == i && y - 1 == j))
                            D.setElmt(row, col, -1);
                        else
                            D.setElmt(row, col, 0);
                        col++;
                    }
                }
                row++;
            }
        }
    }
}
