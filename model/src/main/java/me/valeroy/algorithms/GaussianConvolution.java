package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class GaussianConvolution {
    private static int[][] _kernel = {{1, 2, 3, 2, 1}, {2, 6, 8, 6, 2}, {3, 8, 10, 8, 3}, {2, 6, 8, 6, 2}, {1, 2, 3, 2, 1}};

    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output) {
        for (int y = _kernel.length / 2; y < input.height - (_kernel.length / 2); ++y) {
            for (int x = _kernel.length / 2; x < input.width - (_kernel.length / 2); ++x) {
                int avgRed = 0, avgGreen = 0, avgBlue = 0;
                int a = 0, b = 0;

                for (int j = y - (_kernel.length / 2); j <= y + (_kernel.length / 2); ++j) {
                    for (int i = x - (_kernel.length / 2); i <= x + (_kernel.length / 2); ++i) {
                        int red = input.getBand(0).get(i, j);
                        int green = input.getBand(1).get(i, j);
                        int blue = input.getBand(2).get(i, j);
                        avgRed += red * _kernel[a][b];
                        avgGreen += green * _kernel[a][b];
                        avgBlue += blue * _kernel[a][b];
                        b++;
                    }
                    a++;
                    b = 0;
                }

                avgRed /= 98;
                avgGreen /= 98;
                avgBlue /= 98;
                output.getBand(0).set(x, y, avgRed);
                output.getBand(1).set(x, y, avgGreen);
                output.getBand(2).set(x, y, avgBlue);
            }
        }
    }
}