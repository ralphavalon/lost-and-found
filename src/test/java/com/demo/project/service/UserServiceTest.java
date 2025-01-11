package com.demo.project.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.demo.project.client.UserClient;
import com.demo.project.exceptions.ExternalCallException;
import com.demo.project.model.User;

import feign.FeignException.FeignClientException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {

  private static PodamFactory podamFactory = new PodamFactoryImpl();

  @Autowired
  private UserService userService;

  @MockitoBean
  private UserClient userClient;

  @Test
  public void shouldGetUserData() {
    User user = podamFactory.manufacturePojo(User.class);
    doReturn(user).when(userClient).getUser(anyLong());

    userService.getUser(1L);
  }

  @Test
  public void shouldThrowRightExceptionIfFails() {
    doThrow(FeignClientException.class).when(userClient).getUser(anyLong());

    assertThatThrownBy(() -> {
      userService.getUser(1L);
    }).isInstanceOf(ExternalCallException.class);
  }

}
