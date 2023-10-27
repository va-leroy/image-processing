package me.valeroy;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.valeroy.algorithms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

@RestController
public class SpaController {
    @Autowired
    private ObjectMapper _mapper;

    private final ImageDao _imageDao;

    @Autowired
    public SpaController(ImageDao imageDao) {
        this._imageDao = imageDao;
    }

    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImage(@PathVariable("id") long id) {
        Optional<Image> image = _imageDao.retrieve(id);
        if (image.isPresent()) {
            InputStream inputStream = new ByteArrayInputStream(image.get().getData());
            try {
                String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
                if (mimeType.contains("jpg") || mimeType.contains("jpeg"))
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(inputStream));
                if (mimeType.contains("png"))
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(inputStream));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>("Image ID(" + id + ") not found.", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/images/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteImage(@PathVariable("id") long id) {

        Optional<Image> image = _imageDao.retrieve(id);

        if (image.isPresent()) {
            _imageDao.delete(image.get());
            return new ResponseEntity<>("Image ID(" + id + ") deleted.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Image ID(" + id + ") not found.", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/images", method = RequestMethod.POST)
    public ResponseEntity<?> addImage(@RequestParam("file") MultipartFile file) {

        String contentType = file.getContentType();
        assert contentType != null;
        if (!contentType.equals(MediaType.IMAGE_JPEG.toString()) && !contentType.equals(MediaType.IMAGE_PNG.toString()))
            return new ResponseEntity<>("Only JPEG and PNG file formats supported.", HttpStatus.UNSUPPORTED_MEDIA_TYPE);

        try {
            _imageDao.create(new Image(file.getOriginalFilename(), file.getBytes()));
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to read file.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Image uploaded successfully.", HttpStatus.OK);
    }

    @RequestMapping(value = "/images", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayNode getImageList() {
        List<Image> images = _imageDao.retrieveAll();
        ArrayNode nodes = _mapper.createArrayNode();
        for (Image image : images) {
            ObjectNode objectNode = _mapper.createObjectNode();
            objectNode.put("id", image.getID());
            objectNode.put("name", image.getName());
            objectNode.put("type", image.getMediaType(image.getName()));
            objectNode.put("size", image.getSize(image.getData()));
            nodes.add(objectNode);
        }
        return nodes;
    }

    @RequestMapping(value = "/images", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteImages() {
        return new ResponseEntity<>("Method not allowed.", HttpStatus.METHOD_NOT_ALLOWED);
    }


    @RequestMapping(value = "/images/{id}/{alg}/{param}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImageFromAlgorithms(@PathVariable("id") long id, @PathVariable("alg") String alg, @PathVariable("param") int param) {
        Optional<Image> image = _imageDao.retrieve(id);
        if (image.isEmpty())
            return new ResponseEntity<>("Image ID(" + id + ") not found.", HttpStatus.NOT_FOUND);

        InputStream in = new ByteArrayInputStream(image.get().getData());
        BufferedImage input;
        BufferedImage output;

        try {
            input = ImageIO.read(in);
        } catch (IOException e) {
            return new ResponseEntity<>("Couldn't read image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Planar<GrayU8> before = ConvertBufferedImage.convertFromPlanar(input, null, true, GrayU8.class);
        Planar<GrayU8> after = before.createSameShape();
        ByteArrayOutputStream os;
        InputStream is;

        switch (alg) {
            case "brightness":
            case "histogram":
            case "hue":
            case "mean":
            case "gaussian":
            case "gray":
            case "sobel":
            case "negative":
            case "grain":
                applyImageAlgorithm(alg, before, after, param);
                break;
            case "sepia":
            case "vignette":
            case "noise":
            case "horizontal":
                applyImageAlgorithm(alg, before, after, 0);
                break;
            default:
                return new ResponseEntity<>("This algorithm does not exist.", HttpStatus.BAD_REQUEST);
        }

        output = ConvertBufferedImage.convertTo_U8(after, null, true);
        os = new ByteArrayOutputStream();
        try {
            ImageIO.write(output, "jpeg", os);
        } catch (IOException e) {
            return new ResponseEntity<>("Couldn't convert to ImageIO.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        is = new ByteArrayInputStream(os.toByteArray());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(is));
    }

    private void applyImageAlgorithm(String alg, Planar<GrayU8> before, Planar<GrayU8> after, int param) {
        switch (alg) {
            case "brightness":
                Brightness.compute(before, after, param);
                break;
            case "histogram":
                Histogram.compute(before, after);
                break;
            case "hue":
                Hue.compute(before, after, param);
                break;
            case "mean":
                MeanConvolution.compute(before, after, param);
                break;
            case "gaussian":
                GaussianConvolution.compute(before, after);
                break;
            case "gray":
                Grayscale.compute(before, after);
                break;
            case "sobel":
                Grayscale.compute(before, after);
                Planar<GrayU8> sobel = after.createSameShape();
                Sobel.compute(after, sobel);
                after.setTo(sobel);
                break;
            case "negative":
                Negative.compute(before, after);
                break;
            case "grain":
                Grain.compute(before, after, param);
                break;
            case "sepia":
                Sepia.compute(before, after);
                break;
            case "vignette":
                Vignette.compute(before, after);
                break;
            case "noise":
                Noise.compute(before, after, param);
                break;
            case "horizontal":
                HorizontalFlip.compute(before, after);
                break;
        }
    }
}