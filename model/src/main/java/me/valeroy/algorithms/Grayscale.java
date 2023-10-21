package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Grayscale {
    private static float _red = 0.3f;
    private static float _green = 0.59f;
    private static float _blue = 0.11f;

    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output) {
        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                int red = (int) (input.getBand(0).get(x, y) * _red);
                int green = (int) (input.getBand(1).get(x, y) * _green);
                int blue = (int) (input.getBand(2).get(x, y) * _blue);
                int gray = red + green + blue;
                output.getBand(0).set(x, y, gray);
                output.getBand(1).set(x, y, gray);
                output.getBand(2).set(x, y, gray);
            }
        }
    }
}