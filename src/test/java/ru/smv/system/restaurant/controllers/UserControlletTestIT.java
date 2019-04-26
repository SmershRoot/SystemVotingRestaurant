package ru.smv.system.restaurant.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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

    MockMvc mockMvc;

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
    public void createUser() throws Exception {
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
    }
}
