package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Brightness {
    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output, int delta) {
        delta = Math.max(-255, Math.min(255, delta));
        for (int i = 0; i < input.getNumBands(); i++) {
            GrayU8 inputBand = input.getBand(i);
            GrayU8 outputBand = output.getBand(i);

            for (int y = 0; y < input.height; ++y) {
                for (int x = 0; x < input.width; ++x) {
                    int value = inputBand.get(x, y) + delta;
                    value = Math.max(0, Math.min(255, value));
                    outputBand.set(x, y, value);
                }
            }
        }
    }
}