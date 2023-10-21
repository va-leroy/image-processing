package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

import static me.valeroy.algorithms.Utils.convertHSVtoRGB;
import static me.valeroy.algorithms.Utils.convertRGBtoHSV;

public class Histogram {
    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output) {
        int[] hist = new int[360];

        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                float[] hsv = new float[3];
                int red = input.getBand(0).get(x, y);
                int green = input.getBand(1).get(x, y);
                int blue = input.getBand(2).get(x, y);

                convertRGBtoHSV(red, green, blue, hsv);
                hist[(int) hsv[0]]++;
            }
        }

        int[] cml = new int[360];
        for (int i = 0; i < cml.length; i++)
            for (int j = 0; j < i; j++)
                cml[i] += hist[j];


        int imageSize = input.height * input.width;
        for (int i = 0; i < cml.length; i++) {
            int numerator = 255 * cml[i];
            cml[i] = numerator / imageSize;
        }

        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                float[] hsv = new float[3];
                int[] rgb = new int[3];

                int red = input.getBand(0).get(x, y);
                int green = input.getBand(1).get(x, y);
                int blue = input.getBand(2).get(x, y);

                convertRGBtoHSV(red, green, blue, hsv);
                convertHSVtoRGB(cml[(int) (hsv[0])], hsv[1], hsv[2], rgb);

                output.getBand(0).set(x, y, rgb[0]);
                output.getBand(1).set(x, y, rgb[1]);
                output.getBand(2).set(x, y, rgb[2]);
            }
        }
    }
}