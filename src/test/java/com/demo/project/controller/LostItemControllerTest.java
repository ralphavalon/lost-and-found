package com.demo.project.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.demo.project.config.security.SecurityConfig;
import com.demo.project.model.LostItem;
import com.demo.project.model.User;
import com.demo.project.service.LostItemService;
import com.demo.project.utils.LostItemUtils;
import com.demo.project.utils.UserUtils;

@SpringBootTest
@Import({ SecurityConfig.class })
@WithMockUser(username = "user", roles = { "USER" })
public class LostItemControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @MockitoBean
  private LostItemService lostItemService;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @DisplayName("Should show all lost items without claimedBy")
  @Test
  public void shouldShowAllLostItems() throws Exception {
    List<User> claimedBy = Arrays.asList(UserUtils.user(1L, "name1"), UserUtils.user(2L, "name2"));
    LostItem lostItem = LostItemUtils.lostItemWithClaimedBy(claimedBy);
    doReturn(Arrays.asList(lostItem)).when(lostItemService).getAllLostItems();

    String response = mockMvc.perform(get("/items"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    String expectedResponse = """
        [{
          "id": "##lostItemId##",
          "itemName": "itemName",
          "quantity": 2,
          "place": "place"
        }]
        """.replace("##lostItemId##", lostItem.getId().toString());

    JSONAssert.assertEquals(expectedResponse, response, true);

    verify(lostItemService).getAllLostItems();
  }

  @DisplayName("Should claim lost item")
  @Test
  public void shouldClaimLostItem() throws Exception {
    User user = UserUtils.user(1L, null);

    String request = """
          { "quantity": 2 }
        """;

    mockMvc.perform(post("/items/ea662e0f-d01d-49aa-9559-38e2e50505ba/claim")
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    verify(lostItemService).claimLostItem(eq(UUID.fromString("ea662e0f-d01d-49aa-9559-38e2e50505ba")), eq(2), eq(user));
  }

  @DisplayName("Should not claim lost item on invalid parameters")
  @ParameterizedTest
  @NullSource
  @ValueSource(ints = { 0, -1 })
  public void shouldNotClaimLostItemOnInvalidParameters(Integer quantity) throws Exception {
    String request = String.format("""
          { "quantity": %d }
        """, quantity);

    String response = mockMvc.perform(post("/items/ea662e0f-d01d-49aa-9559-38e2e50505ba/claim")
        .content(request)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn().getResponse().getContentAsString();

    String expectedResponse = """
          {"error":"Invalid Request","message":"Invalid Field(s)","details":{"quantity":"%s"}}
        """;

    if (quantity == null) {
      JSONAssert.assertEquals(String.format(expectedResponse, "must not be null"), response, true);
    } else {
      JSONAssert.assertEquals(String.format(expectedResponse, "must be greater than 0"), response, true);
    }

  }

}
