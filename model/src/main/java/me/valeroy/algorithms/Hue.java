package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

import static me.valeroy.algorithms.Utils.convertHSVtoRGB;
import static me.valeroy.algorithms.Utils.convertRGBtoHSV;

public class Hue {
    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output, float hue) {
        hue = Math.max(0, Math.min(359, hue));
        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                int red = input.getBand(0).get(x, y);
                int green = input.getBand(1).get(x, y);
                int blue = input.getBand(2).get(x, y);

                float[] hsv = new float[3];
                convertRGBtoHSV(red, green, blue, hsv);

                int[] rgb = new int[3];
                convertHSVtoRGB(hue, hsv[1], hsv[2], rgb);

                output.getBand(0).set(x, y, rgb[0]);
                output.getBand(1).set(x, y, rgb[1]);
                output.getBand(2).set(x, y, rgb[2]);
            }
        }
    }
}