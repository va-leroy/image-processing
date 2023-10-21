package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Grain {
    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output, int delta) {
        delta = Math.min(Math.max(delta, 0), 255);
        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                int red = input.getBand(0).get(x, y);
                int green = input.getBand(1).get(x, y);
                int blue = input.getBand(2).get(x, y);

                double grain = Math.random() * delta;

                red = Math.min(Math.max(red + (int) (grain - delta / 2), 0), 255);
                green = Math.min(Math.max(green + (int) (grain - delta / 2), 0), 255);
                blue = Math.min(Math.max(blue + (int) (grain - delta / 2), 0), 255);

                output.getBand(0).set(x, y, red);
                output.getBand(1).set(x, y, green);
                output.getBand(2).set(x, y, blue);
            }
        }
    }
}