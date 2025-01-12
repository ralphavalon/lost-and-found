package com.demo.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.demo.project.entity.LostItemEntity;
import com.demo.project.model.LostItem;
import com.demo.project.model.User;
import com.demo.project.repository.LostItemRepository;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest
public class LostItemServiceTest {

  private static PodamFactory podamFactory = new PodamFactoryImpl();

  @Autowired
  private LostItemService lostItemService;

  @MockitoBean
  private LostItemRepository lostItemRepository;

  @MockitoBean
  private UserService userService;

  @Captor
  private ArgumentCaptor<LostItemEntity> captor;

  @DisplayName("Should register lost item")
  @Test
  public void shouldRegisterLostItem() throws IOException {
    LostItem lostItem = podamFactory.manufacturePojo(LostItem.class);

    lostItemService.registerLostItem(lostItem);

    verify(lostItemRepository).save(captor.capture());
    LostItemEntity resultLostItemEntity = captor.getValue();
    assertThat(resultLostItemEntity.getItemName()).isNotNull().isEqualTo(lostItem.getItemName());
    assertThat(resultLostItemEntity.getQuantity()).isPositive().isEqualTo(lostItem.getQuantity());
    assertThat(resultLostItemEntity.getPlace()).isNotNull().isEqualTo(lostItem.getPlace());
  }

  @DisplayName("Should get all lost items")
  @Test
  public void shouldGetAllLostItems() throws IOException {
    List<LostItemEntity> lostItems = Arrays.asList(
        podamFactory.manufacturePojo(LostItemEntity.class));
    doReturn(lostItems).when(lostItemRepository).findAll();

    List<LostItem> resultAllLostItems = lostItemService.getAllLostItems();

    verify(lostItemRepository).findAll();

    LostItem resultLostItem = resultAllLostItems.get(0);
    LostItemEntity lostItemEntity = lostItems.get(0);

    assertThat(resultLostItem).isNotNull().usingRecursiveComparison().ignoringFields("claimedBy")
        .isEqualTo(lostItemEntity);
    assertThat(resultLostItem.getClaimedBy().size()).isEqualTo(lostItems.get(0).getClaimedBy().size());
    for (int i = 0; i < resultLostItem.getClaimedBy().size(); i++) {
      User user = resultLostItem.getClaimedBy().get(i);
      assertThat(user.getName()).isNull();
    }
  }

}
