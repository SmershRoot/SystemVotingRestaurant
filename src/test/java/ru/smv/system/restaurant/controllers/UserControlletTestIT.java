package ru.smv.system.restaurant.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import ru.smv.system.restaurant.constants.AccessPath;
import ru.smv.system.restaurant.models.dto.UserDTO;
import ru.smv.system.restaurant.utils.TestUtils;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserControlletTestIT {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        setAuthentification();
    }

    private void setAuthentification(){
        TestUtils.getMockHttpSession();
    }

    @Test
    public void getUsers() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(AccessPath.API_USERS);
        ResultActions resultActions = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();

     //   CollectionType typeResult = objectMapper.getTypeFactory().constructCollectionType(List.class, UserDTO.class);
        List<UserDTO> userDTO = objectMapper.readValue(response, new TypeReference<List<UserDTO>>(){});

        Assert.isTrue(userDTO.size() > 0, "Запрос не вернул пользователей");
    }

    @Test
    public void createAndUserAndDeleteUserAndChangePassword() throws Exception {
        UserDTO user = new UserDTO();
        user.setLogin("login");
        user.setPassword("dfd");
        user.setLastName("testLastName");
        user.setFirstName("testFirstName");
        user.setPatronymic("testPatronymic");

        String jsonUserDTO = objectMapper.writeValueAsString(user);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(AccessPath.API_USERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonUserDTO);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        Assert.isTrue(200 == mvcResult.getResponse().getStatus(), "Ошибка создания пользователя");

        String responseUserDTO = mvcResult.getResponse().getContentAsString();
        user = objectMapper.readValue(responseUserDTO, UserDTO.class);

        Assert.isTrue(null != user.getId(), "Ошибка создания пользователя, возвращаемый объект не содержит ИД");

        user.setLastName("testLastName_test");
        user.setFirstName("testFirstName_test");
        user.setPatronymic("testPatronymic_test");
        jsonUserDTO = objectMapper.writeValueAsString(user);

        requestBuilder = MockMvcRequestBuilders.put(AccessPath.API_USERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonUserDTO);
        mvcResult = mockMvc.perform(requestBuilder).andReturn();
        Assert.isTrue(200 == mvcResult.getResponse().getStatus(), "Ошибка обновления пользователя");

        requestBuilder =
                MockMvcRequestBuilders.put(AccessPath.API_USERS_PASSWORD, 38L)
                        .param("current", "dfd")
                        .param("new", "fffffffff");
        mvcResult = mockMvc.perform(requestBuilder).andReturn();
        Assert.isTrue(204 == mvcResult.getResponse().getStatus(), "Ошибка обновления пароля пользователя");

        requestBuilder = MockMvcRequestBuilders.delete(AccessPath.API_USERS_SUD, user.getId());
        mvcResult = mockMvc.perform(requestBuilder).andReturn();
        Assert.isTrue(204 == mvcResult.getResponse().getStatus(), "Ошибка удаления пользователя");
    }

}
