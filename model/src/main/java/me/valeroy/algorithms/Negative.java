package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Negative {
    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output) {
        for (int i = 0; i < input.getNumBands(); i++) {
            GrayU8 inputBand = input.getBand(i);
            GrayU8 outputBand = output.getBand(i);

            for (int y = 0; y < input.height; ++y) {
                for (int x = 0; x < input.width; ++x) {
                    int value = 255 - inputBand.get(x, y);
                    outputBand.set(x, y, value);
                }
            }
        }
    }
}