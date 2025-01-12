package com.demo.project.controller;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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

}
