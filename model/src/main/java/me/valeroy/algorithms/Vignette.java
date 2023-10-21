package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Vignette {
    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output) {
        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                int red = input.getBand(0).get(x, y);
                int green = input.getBand(1).get(x, y);
                int blue = input.getBand(2).get(x, y);

                double d = Math.sqrt(Math.pow(x - input.width / 2, 2) + Math.pow(y - input.height / 2, 2));
                double d2 = Math.sqrt(Math.pow(input.width / 2, 2) + Math.pow(input.height / 2, 2));
                double d3 = d / d2;
                double d4 = 1 - d3;

                red = (int) (red * d4);
                green = (int) (green * d4);
                blue = (int) (blue * d4);

                output.getBand(0).set(x, y, red);
                output.getBand(1).set(x, y, green);
                output.getBand(2).set(x, y, blue);
            }
        }
    }
}