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
import ru.smv.system.restaurant.models.dto.MenuDTO;
import ru.smv.system.restaurant.models.dto.RestaurantDTO;
import ru.smv.system.restaurant.utils.TestUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class RestaurantControllerTestIT {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        setAuthentification();
    }

    private void setAuthentification(){
        TestUtils.getMockHttpSession();
    }

    @Test
    void getRestaurants() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(AccessPath.API_RESTAURANTS);
        ResultActions resultActions = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();

        //   CollectionType typeResult = objectMapper.getTypeFactory().constructCollectionType(List.class, RestaurantDTO.class);
        List<RestaurantDTO> restaurantsDTO = objectMapper.readValue(response, new TypeReference<List<RestaurantDTO>>(){});

        Assert.isTrue(restaurantsDTO.size() > 0, "Запрос не вернул рестораны");
    }

    @Test
    void getRestaurant() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(AccessPath.API_RESTAURANTS_SUD, 1L);
        ResultActions resultActions = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();
    }

    @Test
    void createAndUpdateAndDeleteRestaurant() throws Exception {
        RestaurantDTO restaurant = new RestaurantDTO();
        restaurant.setAddress("testAddress");
        restaurant.setEmail("testEmail");
        restaurant.setName("testName");
        restaurant.setNote("testNote");
        restaurant.setPhone("testPhone");
        restaurant.setTimeWork("testTimeWork");

        String jsonRestaurantDTO = objectMapper.writeValueAsString(restaurant);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(AccessPath.API_RESTAURANTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRestaurantDTO);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        Assert.isTrue(200 == mvcResult.getResponse().getStatus(), "Ошибка создания ресторана");

        String responseRestaurantDTO = mvcResult.getResponse().getContentAsString();
        restaurant = objectMapper.readValue(responseRestaurantDTO, RestaurantDTO.class);

        Assert.isTrue(null != restaurant.getId(), "Ошибка создания ресторана, возвращаемый объект не содержит ИД");

        Set<MenuDTO> menus = new HashSet<>();
        for(int i=0;i<3;i++) {
            MenuDTO menu = new MenuDTO();
            menu.setDateMenu(LocalDate.now());
            menu.setName("testMenu:" + i);
            menu.setNote("testNote:" + i);
            menu.setPhotoSrc("testSrc:" + i);
            menus.add(menu);
        }
        restaurant.setName("testWithMenu");
        restaurant.setMenus(menus);
        jsonRestaurantDTO = objectMapper.writeValueAsString(restaurant);

        requestBuilder = MockMvcRequestBuilders.put(AccessPath.API_RESTAURANTS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRestaurantDTO);
        mvcResult = mockMvc.perform(requestBuilder).andReturn();
        Assert.isTrue(200 == mvcResult.getResponse().getStatus(), "Ошибка обновления ресторана");

        requestBuilder = MockMvcRequestBuilders.delete(AccessPath.API_RESTAURANTS_SUD, restaurant.getId());
        mvcResult = mockMvc.perform(requestBuilder).andReturn();
        Assert.isTrue(204 == mvcResult.getResponse().getStatus(), "Ошибка удаления ресторана");
    }
}