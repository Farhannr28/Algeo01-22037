package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageInterpolation {
    public static Scanner input = new Scanner(System.in); // string
    public static Scanner masukan = new Scanner(System.in); // double

    public static void main(String[] args) throws IOException {
        // Declare
        Matrix X = new Matrix(16, 16);
        Matrix D = new Matrix(16, 16);
        Matrix inverseX = new Matrix(16, 16);
        Matrix multiplier = new Matrix(16, 16);

        // Construct X and D
        InterpolasiBikubik.constructX(X);
        ImageInterpolation.constructD(D);

        // inverse matrix and save it to inverseX
        inverseX.copyMatrix(X);
        inverseX.InversWithGaussJordan();

        // multiply X and D and save it to multiplier
        multiplier = Matrix.multiplyMatrix(inverseX, D);
        for (int i = 0; i < multiplier.getRow(); i++) {
            for (int j = 0; j < multiplier.getCol(); j++) {
                multiplier.setElmt(i, j, multiplier.getElmt(i, j) / 4);
            }
        }

        // get image file
        String fileName = readfileName();
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
        System.out.print(">> Enter upscale of your image: ");
        double k = masukan.nextDouble();

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
                    if (r >= 0 && r <= 255)
                        rgb += (int) Math.round(r);
                    else if (r > 255)
                        rgb += 255;
                    rgb <<= 8;
                    if (g >= 0 && g <= 255)
                        rgb += (int) Math.round(g);
                    else if (g > 255) {
                        rgb += 255;
                    }
                    rgb <<= 8;
                    if (b >= 0 && b <= 255)
                        rgb += (int) Math.round(b);
                    else if (b > 255) {
                        rgb += 255;
                    }
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
        try {
            System.out.print(">> Enter output file name: ");
            String outputFileName = input.nextLine();
            File outputImg = new File("test/" + outputFileName);
            ImageIO.write(newImage, "png", outputImg);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error");
            System.exit(0);
        }
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

    public static String readfileName() {
        Scanner sc;
        String fileName = "";
        while (true) {
            try {
                System.out.print(">> Enter input file name: ");
                fileName = input.nextLine();
                File file = new File("test/" + fileName);
                sc = new Scanner(file);
                break;

            } catch (FileNotFoundException e) {
                System.out.println("There is no file with name " + fileName);
            }
        }
        sc.close();
        return "test/" + fileName;
    }
}
