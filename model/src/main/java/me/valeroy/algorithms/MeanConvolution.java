package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class MeanConvolution {
    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output, int size) {
        size = Math.max(size, 1);
        size = Math.min(size, Math.min(input.height, input.width));
        size = (size % 2 == 0) ? size + 1 : size;

        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                int avgRed = 0, avgGreen = 0, avgBlue = 0;

                for (int j = Math.max(0, y - size / 2); j <= Math.min(input.height - 1, y + size / 2); ++j) {
                    for (int i = Math.max(0, x - size / 2); i <= Math.min(input.width - 1, x + size / 2); ++i) {
                        int red = input.getBand(0).get(i, j);
                        int green = input.getBand(1).get(i, j);
                        int blue = input.getBand(2).get(i, j);
                        avgRed += red;
                        avgGreen += green;
                        avgBlue += blue;
                    }
                }

                int divisor = size * size;
                output.getBand(0).set(x, y, avgRed / divisor);
                output.getBand(1).set(x, y, avgGreen / divisor);
                output.getBand(2).set(x, y, avgBlue / divisor);
            }
        }
    }
}