package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Noise {
    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output, int delta) {
        delta = Math.min(Math.max(delta, 0), 255);
        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                int red = input.getBand(0).get(x, y);
                int green = input.getBand(1).get(x, y);
                int blue = input.getBand(2).get(x, y);

                double noiseRed = Math.random() * delta;
                double noiseGreen = Math.random() * delta;
                double noiseBlue = Math.random() * delta;

                red = (int) (red + noiseRed - delta / 2);
                green = (int) (green + noiseGreen - delta / 2);
                blue = (int) (blue + noiseBlue - delta / 2);

                red = Math.min(Math.max(red, 0), 255);
                green = Math.min(Math.max(green, 0), 255);
                blue = Math.min(Math.max(blue, 0), 255);

                output.getBand(0).set(x, y, red);
                output.getBand(1).set(x, y, green);
                output.getBand(2).set(x, y, blue);
            }
        }
    }
}