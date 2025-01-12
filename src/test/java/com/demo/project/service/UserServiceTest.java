package com.demo.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.demo.project.client.UserClient;
import com.demo.project.exceptions.ExternalCallException;
import com.demo.project.model.LostItem;
import com.demo.project.model.User;
import com.demo.project.repository.UserRepository;
import com.demo.project.utils.LostItemUtils;

import feign.FeignException.FeignClientException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {

  private static PodamFactory podamFactory = new PodamFactoryImpl();

  @MockitoSpyBean
  private UserService userService;

  @MockitoBean
  private UserRepository userRepository;

  @MockitoBean
  private UserClient userClient;

  @Test
  public void shouldGetUserData() {
    User user = podamFactory.manufacturePojo(User.class);
    doReturn(user).when(userClient).getUser(anyLong());

    userService.getUserAdditionalData(1L);
  }

  @Test
  public void shouldThrowRightExceptionIfFails() {
    doThrow(FeignClientException.class).when(userClient).getUser(anyLong());

    assertThatThrownBy(() -> {
      userService.getUserAdditionalData(1L);
    }).isInstanceOf(ExternalCallException.class);
  }

  @DisplayName("Should get user names")
  @Test
  public void shouldGetAllLostItems() throws IOException {
    LostItem lostItem = LostItemUtils.lostItemWithClaimedBy(
      Arrays.asList(User.builder().id(1L).build(), User.builder().id(2L).build()));

    User userOne = User.builder().id(1L).name("User One").build();
    User userTwo = User.builder().id(2L).name("User Two").build();
    doReturn(userOne, userTwo).when(userService).getUserAdditionalData(anyLong());

    List<User> resultAllUsers = userService.getAllUsersNames(lostItem);

    verify(userService, times(2)).getUserAdditionalData(anyLong());

    assertThat(resultAllUsers.size()).isEqualTo(lostItem.getClaimedBy().size());
    assertThat(resultAllUsers.get(0).getName()).isNotNull().isEqualTo(userOne.getName());
    assertThat(resultAllUsers.get(1).getName()).isNotNull().isEqualTo(userTwo.getName());
  }

}
