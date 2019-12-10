package edu.umich.kanboard.kanboardapi.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umich.kanboard.CustomGlobalExceptionHandler;
import edu.umich.kanboard.user.UserController;
import edu.umich.kanboard.user.UserDetailsServiceImpl;
import edu.umich.kanboard.user.UserEntity;
import edu.umich.kanboard.user.UserExceptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("TEST")
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserControllerTest {

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new CustomGlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    public void registerNewUser_success() throws Exception {
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        Mockito.when(userDetailsService.createUser(user)).thenReturn(user);
        post("/register", objectMapper.writeValueAsString(user)).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void registerNewUser_userAlreadyExistsThrowsException() throws Exception {
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        Mockito.when(userDetailsService.createUser(user)).thenThrow(new UserExceptions.UserAlreadyExistsException(user.getUsername()));
        post("/register", objectMapper.writeValueAsString(user)).andExpect(status().isBadRequest()).andReturn();
    }

    private ResultActions post(String url, String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }
}