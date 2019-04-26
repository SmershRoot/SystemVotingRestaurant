package ru.smv.system.restaurant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import ru.smv.system.restaurant.utils.TestUtils;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class VotingControllerTestIT {

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
    public void getResultVoting() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(AccessPath.API_RESTAURANTS_VOTING, LocalDate.now().toString());
        ResultActions resultActions = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();
    }

    @Test
    public void voting() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(AccessPath.API_RESTAURANTS_SUD_VOTING, 1L);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        Assert.isTrue(204 == mvcResult.getResponse().getStatus(), "Ошибка создания записи об голосованити");
    }
}
