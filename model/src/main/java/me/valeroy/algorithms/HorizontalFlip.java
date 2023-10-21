package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class HorizontalFlip {
    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output) {
        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                output.getBand(0).set(input.width - x - 1, y, input.getBand(0).get(x, y));
                output.getBand(1).set(input.width - x - 1, y, input.getBand(1).get(x, y));
                output.getBand(2).set(input.width - x - 1, y, input.getBand(2).get(x, y));
            }
        }
    }
}