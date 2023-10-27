package me.valeroy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class SpaControllerTests {

    @Autowired
    private MockMvc _mockMvc;

    @BeforeAll
    public static void reset() {
        /* Reset the counter to 0 */
        ReflectionTestUtils.setField(Image.class, "_count", 0L);
    }

    @Test
    @Order(1)
    public void getImageListShouldReturnSuccess() throws Exception {
        this._mockMvc.perform(get("/images")).andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void getImageShouldReturnNotFound() throws Exception {
        /* We assume that there is no image at index 5000 */
        this._mockMvc.perform(get("/images/5000")).andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    public void getImageShouldReturnSuccess() throws Exception {
        /* We assume that there is always an image at index 0 */
        this._mockMvc.perform(get("/images/0")).andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void deleteImagesShouldReturnMethodNotAllowed() throws Exception {
        this._mockMvc.perform(delete("/images")).andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Order(5)
    public void deleteImageShouldReturnNotFound() throws Exception {
        this._mockMvc.perform(delete("/images/5001")).andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    public void deleteImageShouldReturnSuccess() throws Exception {
        this._mockMvc.perform(delete("/images/0")).andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void createImageShouldReturnSuccess() throws Exception {
        final ClassPathResource cpr = new ClassPathResource("test.jpg");
        MockMultipartFile mmf = new MockMultipartFile("file", "test.jpg", "image/jpeg", Files.readAllBytes(cpr.getFile().toPath()));
        this._mockMvc.perform(MockMvcRequestBuilders.multipart("/images").file(mmf)).andExpect(status().isOk());
    }

    @Test
    @Order(8)
    public void createImageShouldReturnUnsupportedMediaType() throws Exception {
        final ClassPathResource cpr = new ClassPathResource("test.jpg");
        MockMultipartFile mmf = new MockMultipartFile("file", "test.jpg", "image/xcx", Files.readAllBytes(cpr.getFile().toPath()));
        this._mockMvc.perform(MockMvcRequestBuilders.multipart("/images").file(mmf)).andExpect(status().isUnsupportedMediaType());
    }
}