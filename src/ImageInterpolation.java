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
        Matrix.InversWithGaussJordan(inverseX);

        // multiply X and D and save it to multiplier
        multiplier = Matrix.multiplyMatrix(inverseX, D);
        for (int i = 0; i < multiplier.getRow(); i++) {
            for (int j = 0; j < multiplier.getCol(); j++) {
                multiplier.setElmt(i, j, multiplier.getElmt(i, j) / 4);
            }
        }

        // get image file
        String fileName = IO.readfileName();
        File file = new File(fileName);
        BufferedImage img = ImageIO.read(file);

        Matrix red = new Matrix(img.getHeight() + 4, img.getWidth() + 4);
        Matrix blue = new Matrix(img.getHeight() + 4, img.getWidth() + 4);
        Matrix green = new Matrix(img.getHeight() + 4, img.getWidth() + 4);
        Matrix tempRed = new Matrix(16, 1);
        Matrix tempBlue = new Matrix(16, 1);
        Matrix tempGreen = new Matrix(16, 1);

        Matrix[][] redRes = new Matrix[img.getHeight() + 1][img.getWidth() + 1];
        Matrix[][] blueRes = new Matrix[img.getHeight() + 1][img.getWidth() + 1];
        Matrix[][] greenRes = new Matrix[img.getHeight() + 1][img.getWidth() + 1];

        for (int i = 2; i < img.getHeight() + 2; i++) {
            for (int j = 2; j < img.getWidth() + 2; j++) {
                int pixel = img.getRGB(j - 2, i - 2);
                Color color = new Color(pixel, true);
                red.setElmt(i, j, color.getRed());
                blue.setElmt(i, j, color.getBlue());
                green.setElmt(i, j, color.getGreen());
            }
        }

        for (int i = 2; i < img.getHeight() + 2; i++) {
            red.setElmt(i, 0, red.getElmt(i, 2));
            green.setElmt(i, 0, green.getElmt(i, 2));
            blue.setElmt(i, 0, blue.getElmt(i, 2));
            red.setElmt(i, 1, red.getElmt(i, 2));
            green.setElmt(i, 1, green.getElmt(i, 2));
            blue.setElmt(i, 1, blue.getElmt(i, 2));
            red.setElmt(i, img.getWidth() + 2, red.getElmt(i, img.getWidth() + 1));
            green.setElmt(i, img.getWidth() + 2, green.getElmt(i, img.getWidth() + 1));
            blue.setElmt(i, img.getWidth() + 2, blue.getElmt(i, img.getWidth() + 1));
            red.setElmt(i, img.getWidth() + 3, red.getElmt(i, img.getWidth() + 1));
            green.setElmt(i, img.getWidth() + 3, green.getElmt(i, img.getWidth() + 1));
            blue.setElmt(i, img.getWidth() + 3, blue.getElmt(i, img.getWidth() + 1));
        }

        for (int j = 2; j < img.getWidth() + 2; j++) {
            red.setElmt(0, j, red.getElmt(2, j));
            green.setElmt(0, j, green.getElmt(2, j));
            blue.setElmt(0, j, blue.getElmt(2, j));
            red.setElmt(1, j, red.getElmt(2, j));
            green.setElmt(1, j, green.getElmt(2, j));
            blue.setElmt(1, j, blue.getElmt(2, j));
            red.setElmt(img.getHeight() + 2, j, red.getElmt(img.getHeight() + 1, j));
            green.setElmt(img.getHeight() + 2, j, green.getElmt(img.getHeight() + 1, j));
            blue.setElmt(img.getHeight() + 2, j, blue.getElmt(img.getHeight() + 1, j));
            red.setElmt(img.getHeight() + 3, j, red.getElmt(img.getHeight() + 1, j));
            green.setElmt(img.getHeight() + 3, j, green.getElmt(img.getHeight() + 1, j));
            blue.setElmt(img.getHeight() + 3, j, blue.getElmt(img.getHeight() + 1, j));
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                red.setElmt(i, j, red.getElmt(2, 2));
                green.setElmt(i, j, green.getElmt(2, 2));
                blue.setElmt(i, j, blue.getElmt(2, 2));
            }
        }

        for (int i = img.getHeight() + 2; i < img.getHeight() + 4; i++) {
            for (int j = 0; j < 2; j++) {
                red.setElmt(i, j, red.getElmt(img.getHeight() + 1, 2));
                green.setElmt(i, j, green.getElmt(img.getHeight() + 1, 2));
                blue.setElmt(i, j, blue.getElmt(img.getHeight() + 1, 2));
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = img.getWidth() + 2; j < img.getWidth() + 4; j++) {
                red.setElmt(i, j, red.getElmt(2, img.getWidth() + 1));
                green.setElmt(i, j, green.getElmt(2, img.getWidth() + 1));
                blue.setElmt(i, j, blue.getElmt(2, img.getWidth() + 1));
            }
        }

        for (int i = img.getHeight() + 2; i < img.getHeight() + 4; i++) {
            for (int j = img.getWidth() + 2; j < img.getWidth() + 4; j++) {
                red.setElmt(i, j, red.getElmt(img.getHeight() + 1, img.getWidth() + 1));
                green.setElmt(i, j, green.getElmt(img.getHeight() + 1, img.getWidth() + 1));
                blue.setElmt(i, j, blue.getElmt(img.getHeight() + 1, img.getWidth() + 1));
            }
        }
        for (int i = 1; i <= img.getHeight() + 1; i++) {
            for (int j = 1; j <= img.getWidth() + 1; j++) {
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
        System.out.print(">> Masukkan perbesaran gambar: ");
        double k = IO.input.nextDouble();

        long newHeight = Math.round(k * img.getHeight());
        long newWidth = Math.round(k * img.getWidth());
        BufferedImage newImage = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {

                double conX = ((double) img.getHeight() / (double) newHeight) * ((double) (2 * i + 1) / (double) 2)
                        + 1.5;
                double conY = ((double) img.getWidth() / (double) newWidth) * ((double) (2 * j + 1) / (double) 2) + 1.5;
                int x = (int) Math.floor(conX) - 1;
                conX -= Math.floor(conX);
                int y = (int) Math.floor(conY) - 1;
                conY -= Math.floor(conY);
                double r = calcRGB(redRes[x][y], conX, conY);
                double g = calcRGB(greenRes[x][y], conX, conY);
                double b = calcRGB(blueRes[x][y], conX, conY);
                int rgb = 0;
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
            }
        }
        try {
            System.out.print(">> Masukkan nama file keluaran: ");
            String outputFileName = IO.scan.nextLine();
            File outputImg = new File("test/output/" + outputFileName);
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
}
