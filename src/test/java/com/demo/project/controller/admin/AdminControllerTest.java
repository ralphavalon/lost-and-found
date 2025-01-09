package com.demo.project.controller.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.demo.project.config.security.SecurityConfig;
import com.demo.project.model.LostItem;
import com.demo.project.parser.LostItemParser;

@SpringBootTest
@Import({ SecurityConfig.class })
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class AdminControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @MockitoBean
  private LostItemParser lostItemParser;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @DisplayName("Should upload valid file")
  @Test
  public void shouldUploadValidFile() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv",
        "abc,1,bca".getBytes());

    doReturn(Arrays.asList(LostItem.builder().build())).when(lostItemParser).parse(any(InputStream.class));

    String response = mockMvc.perform(multipart("/admin/upload")
        .file(file)
        // .header("Authorization","Basic YWRtaW46cGFzc3dvcmQ=")
        .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    String expectedResponse = """
        { "processedRecords": 1 }
        """;

    JSONAssert.assertEquals(expectedResponse, response, true);

    verify(lostItemParser).parse(any(InputStream.class));
  }

  @DisplayName("Should not upload invalid file")
  @ParameterizedTest
  @MethodSource("invalidCSV")
  public void shouldNotUploadInvalidFile(String originalFilename, String contentType) throws Exception {
    MockMultipartFile file = new MockMultipartFile("file", originalFilename, contentType, "abc,1,bca".getBytes());

    String response = mockMvc.perform(multipart("/admin/upload")
        .file(file)
        .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isUnprocessableEntity())
        .andReturn().getResponse().getContentAsString();

    String expectedResponse = """
        { "error": "Error processing file", "message": "Not an acceptable file" }
        """;

    JSONAssert.assertEquals(expectedResponse, response, true);

    verifyNoInteractions(lostItemParser);
  }

  @DisplayName("Should return error message if processing fails")
  @Test
  public void shouldReturnErrorMessageIfProcessingFails() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "abc,1,bca".getBytes());

    doThrow(new RuntimeException("Something went wrong")).when(lostItemParser).parse(any(InputStream.class));

    String response = mockMvc.perform(multipart("/admin/upload")
        .file(file)
        .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isUnprocessableEntity())
        .andReturn().getResponse().getContentAsString();

    String expectedResponse = """
        { "error": "Error processing file", "message": "Something went wrong" }
        """;

    JSONAssert.assertEquals(expectedResponse, response, true);

    verify(lostItemParser).parse(any(InputStream.class));
  }

  static Stream<Arguments> invalidCSV() {
    return Stream.of(
        Arguments.of("test.pdf", "text/csv"),
        Arguments.of("test.pdf", "text/pdf"),
        Arguments.of("test.csv", "text/pdf"),
        Arguments.of("test.csv", "application/csv"));
  }

}
