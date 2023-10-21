package me.valeroy;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ImageDao implements Dao<Image> {
    private final Map<Long, Image> images = new HashMap<>();

    public ImageDao() {
        byte[] fileContent;
        List<Path> names = getAllImagesInDirectory();

        for (Path p : names) {
            try {
                fileContent = Files.readAllBytes(p);

                String[] filename = p.toString().split("/"); // Splits the absolute path
                Image img = new Image(filename[filename.length - 1].trim(), fileContent);

                images.put(img.getID(), img);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Image> retrieve(final long id) {
        return Optional.ofNullable(images.get(id));
    }

    @Override
    public List<Image> retrieveAll() {
        return new ArrayList<>(images.values());
    }

    @Override
    public void create(final Image img) {
        images.put(img.getID(), img);
    }

    @Override
    public void update(final Image img, final String[] params) {
        img.setName(Objects.requireNonNull(params[0], "Name cannot be null"));

        images.put(img.getID(), img);
    }

    @Override
    public void delete(final Image img) {
        images.remove(img.getID());
    }

    public List<Path> getAllImagesInDirectory() {
        List<Path> names = null;
        try {
            String dir = String.valueOf(Paths.get(ClassLoader.getSystemResource("images/").toURI()));
            names = Files.find(Paths.get(dir), 5000, (path, basicFileAttributes) ->
                            basicFileAttributes.isRegularFile() && (path.getFileName().toString().matches(".*\\.jpg")) || path.getFileName().toString().matches(".*\\.png"))
                    .collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return names;
    }
}