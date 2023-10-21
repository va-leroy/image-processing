package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Sobel {
    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output) {
        int[][] hor = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] ver = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        for (int y = 1; y < input.height - 2; ++y) {
            for (int x = 1; x < input.width - 2; ++x) {
                int gxRed = 0;
                int gyRed = 0;

                int gxBlue = 0;
                int gyBlue = 0;

                int gxGreen = 0;
                int gyGreen = 0;

                int a = 0;
                int b = 0;

                for (int j = y - 1; j <= y + 1; ++j) {
                    for (int i = x - 1; i <= x + 1; ++i) {
                        int red = input.getBand(0).get(i, j);
                        int green = input.getBand(1).get(i, j);
                        int blue = input.getBand(2).get(i, j);
                        gxRed += red * hor[a][b];
                        gyRed += red * ver[a][b];

                        gxBlue += blue * hor[a][b];
                        gyBlue += blue * ver[a][b];

                        gxGreen += green * hor[a][b];
                        gyGreen += green * ver[a][b];
                        b++;
                    }
                    a++;
                    b = 0;
                }

                double normRed = Math.sqrt((gxRed * gxRed) + (gyRed * gyRed));
                double normGreen = Math.sqrt((gxGreen * gxGreen) + (gyGreen * gyGreen));
                double normBlue = Math.sqrt((gxBlue * gxBlue) + (gyBlue * gyBlue));

                normRed = Math.min(255, (int) normRed);
                normGreen = Math.min(255, (int) normGreen);
                normBlue = Math.min(255, (int) normBlue);

                output.getBand(0).set(x, y, (int) normRed);
                output.getBand(1).set(x, y, (int) normGreen);
                output.getBand(2).set(x, y, (int) normBlue);
            }
        }
    }
}