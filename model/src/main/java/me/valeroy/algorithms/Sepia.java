package me.valeroy.algorithms;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

public class Sepia {
    public static void compute(Planar<GrayU8> input, Planar<GrayU8> output) {
        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                int red = input.getBand(0).get(x, y);
                int green = input.getBand(1).get(x, y);
                int blue = input.getBand(2).get(x, y);

                int tr = (int) (0.393 * red + 0.769 * green + 0.189 * blue);
                int tg = (int) (0.349 * red + 0.686 * green + 0.168 * blue);
                int tb = (int) (0.272 * red + 0.534 * green + 0.131 * blue);

                tr = Math.min(255, Math.max(0, tr));
                tg = Math.min(255, Math.max(0, tg));
                tb = Math.min(255, Math.max(0, tb));

                output.getBand(0).set(x, y, tr);
                output.getBand(1).set(x, y, tg);
                output.getBand(2).set(x, y, tb);
            }
        }
    }
}