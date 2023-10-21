package me.valeroy;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class Image {
    private static Long _count = 0L;
    private Long _id;
    private String _name;
    private byte[] _data;

    public Image(final String name, final byte[] data) {
        _id = _count++;
        this._name = name;
        this._data = data;
    }

    public long getID() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }

    public byte[] getData() {
        return _data;
    }

    public String getMediaType(String name) {
        if (!name.isEmpty() && !name.isBlank()) {
            String[] extension = name.split("\\.(?=[^\\.]+$)");
            switch (extension[1]) {
                case "png":
                    return "IMAGE_PNG";
                case "jpg":
                case "jpeg":
                    return "IMAGE_JPEG";
                default:
                    return "UNSUPPORTED_MEDIA_TYPE";
            }
        } else {
            return "UNSUPPORTED_MEDIA_TYPE";
        }
    }

    public String getSize(byte[] data) {
        try {
            InputStream is = new ByteArrayInputStream(data);
            BufferedImage image = ImageIO.read(is);
            Planar<GrayU8> p = ConvertBufferedImage.convertFromPlanar(image, null, true, GrayU8.class);
            return image.getWidth() + "*" + image.getHeight() + "*" + p.getNumBands();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}