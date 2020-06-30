package com.pattabhi;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pattabhi.controller.UsersController;
import com.pattabhi.model.User;
import com.pattabhi.repository.UserJpaRespository;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
public class UsersControllerTest {
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserJpaRespository userJpaRespository;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("virat@test.com");
        user.setName("Virat");
        user.setDateOfBirth("01-01-2001");
    }

    @Test
    public void userCreationTest() throws Exception {
        when(userJpaRespository.findByEmail(user.getEmail())).thenReturn(user);
        ObjectMapper mapper = new ObjectMapper();
        String transactionString = mapper.writeValueAsString(user);


        MvcResult result = mockMvc.perform(post("/api/users/load").content(transactionString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String responseUser = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(responseUser, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void findAllUsersTest() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userJpaRespository.findAll()).thenReturn(users);

        MvcResult result = mockMvc.perform(get("/api/users/all")).andExpect(status().isOk()).andReturn();
        JSONAssert.assertEquals(new ObjectMapper().writeValueAsString(users),
                result.getResponse().getContentAsString(), false);
    }

    @Test
    @Ignore
    public void findUserByEmailTest() throws Exception {
        when(userJpaRespository.findByEmail(user.getEmail())).thenReturn(user);
        MvcResult result = mockMvc.perform(get("/api/users/{email}", "virat@test.com")).andExpect(status().isOk()).andReturn();
        JSONAssert.assertEquals(new ObjectMapper().writeValueAsString(user), result.getResponse().getContentAsString(), false);
    }

    @Test
    public void findUserByNameWithWrongEmailTest() throws Exception {
        when(userJpaRespository.findByEmail(user.getEmail())).thenReturn(user);
        MvcResult result = mockMvc.perform(get("/api/users/{email}", "rohit@test.com")).andExpect(status().isOk()).andReturn();
        System.out.println(":::::" + result.getResponse().getContentAsString());
        assertThat(result.getResponse().getContentAsString().length(), is(0));
    }

}
