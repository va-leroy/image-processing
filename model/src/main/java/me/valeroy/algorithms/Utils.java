package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import org.ejml.simple.UnsupportedOperation;

import java.util.Arrays;
import java.util.Collections;

public class Utils {
    public static void convertRGBtoHSV(int r, int g, int b, float[] hsv) {
        float h, s, v;
        int min = Collections.min(Arrays.asList(r, g, b));
        int max = Collections.max(Arrays.asList(r, g, b));
        int delta = max - min;

        if (delta == 0)
            h = 0;
        else if (max == r)
            h = (60 * ((g - b) / (float) delta) + 360) % 360;
        else if (max == g)
            h = 60 * ((b - r) / (float) delta) + 120;
        else if (max == b)
            h = 60 * ((r - g) / (float) (delta)) + 240;
        else
            throw new UnsupportedOperation("Couldn't process RGB values.");

        if (max == 0)
            s = 0;
        else
            s = 1 - (min / (float) (max));

        v = (max / (float) (255)) * 100;
        hsv[0] = h;
        hsv[1] = s * 100;
        hsv[2] = v;
    }

    public static void convertHSVtoRGB(float h, float s, float v, int[] rgb) {
        float red, green, blue;

        h /= 360f;
        s /= 100f;
        v /= 100f;

        float f = h * 6;
        if (f == 6) f = 0;

        int i = (int) Math.floor(f);
        float l = v * (1 - s);
        float m = v * (1 - s * (f - i));
        float n = v * (1 - s * (1 - (f - i)));

        switch (i) {
            case 0:
                red = v;
                green = n;
                blue = l;
                break;
            case 1:
                red = m;
                green = v;
                blue = l;
                break;
            case 2:
                red = l;
                green = v;
                blue = n;
                break;
            case 3:
                red = l;
                green = m;
                blue = v;
                break;
            case 4:
                red = n;
                green = l;
                blue = v;
                break;
            case 5:
                red = v;
                green = l;
                blue = m;
                break;
            default:
                throw new UnsupportedOperation("Couldn't process HSV values.");
        }

        red *= 255;
        green *= 255;
        blue *= 255;

        rgb[0] = (int) red;
        rgb[1] = (int) green;
        rgb[2] = (int) blue;
    }

    @SuppressWarnings("unused")
    public static int[] extremum(Planar<GrayU8> input) {
        int[] ex = new int[6];
        int[] min = {255, 255, 255};
        int[] max = {0, 0, 0};

        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                for (int band = 0; band < 3; ++band) {
                    int value = input.getBand(band).get(x, y);
                    min[band] = Math.min(min[band], value);
                    max[band] = Math.max(max[band], value);
                }
            }
        }
        System.arraycopy(min, 0, ex, 0, 3);
        System.arraycopy(max, 0, ex, 3, 3);
        return ex;
    }
}