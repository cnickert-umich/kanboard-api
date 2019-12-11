package edu.umich.kanboard.userstory;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umich.kanboard.CustomGlobalExceptionHandler;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserStoryControllerTest {

    @Mock
    UserStoryService userStoryService;

    @InjectMocks
    private UserStoryController userStoryController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userStoryController)
                .setControllerAdvice(new CustomGlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    public void getAllUserStories_success() throws Exception {
        List<UserStoryEntity> userStories = new ArrayList<>();
        for (int i = 0; i < Math.random() * 10; i++) {
            userStories.add(new UserStoryEntity());
        }
        when(userStoryService.getAllUserStories()).thenReturn(userStories);
        get("/us").andExpect(status().isOk()).andReturn();
    }

    @Test
    public void getUserStory_notFound() throws Exception {
        final long userStoryId = 1;
        when(userStoryService.getUserStoryById(userStoryId)).thenThrow(UserStoryExceptions.UserStoryNotFound.class);
        get("/us/" + userStoryId).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void saveUserStory_nameNotSpecifiedBadRequest() throws Exception {
        UserStoryEntity userStory = new UserStoryEntity();
        userStory.setName(null);
        when(userStoryService.saveUserStory(userStory)).thenThrow(UserStoryExceptions.UserStoryInvalidNameException.class);
        post("/us", objectMapper.writeValueAsString(userStory)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void saveUserStory_invalidDescriptionBadRequest() throws Exception {
        UserStoryEntity userStory = new UserStoryEntity();
        userStory.setName(null);
        when(userStoryService.saveUserStory(userStory)).thenThrow(UserStoryExceptions.UserStoryInvalidDescriptionException.class);
        post("/us", objectMapper.writeValueAsString(userStory)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void saveUserStory_priorityTooHighOrLowBadRequest() throws Exception {
        UserStoryEntity userStory = new UserStoryEntity();
        userStory.setName(null);
        when(userStoryService.saveUserStory(userStory)).thenThrow(UserStoryExceptions.UserStoryBadPriorityException.class);
        post("/us", objectMapper.writeValueAsString(userStory)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void saveUserStory_happyPath() throws Exception {
        UserStoryEntity userStory = new UserStoryEntity();
        userStory.setName(null);
        when(userStoryService.saveUserStory(userStory)).thenReturn(userStory);
        post("/us", objectMapper.writeValueAsString(userStory)).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void deleteUserStory_userStoryNotFound() throws Exception {
        UserStoryEntity userStory = new UserStoryEntity();
        userStory.setName(null);
        userStory.setUserStoryId((long) 1);
        Mockito.doThrow(UserStoryExceptions.UserStoryNotFound.class).when(userStoryService).deleteUserStory(userStory.getUserStoryId());
        delete("/us/" + userStory.getUserStoryId(), objectMapper.writeValueAsString(userStory)).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void deleteUserStory_happyPath() throws Exception {
        UserStoryEntity userStory = new UserStoryEntity();
        userStory.setName(null);
        userStory.setUserStoryId((long) 1);
        Mockito.doNothing().when(userStoryService).deleteUserStory(userStory.getUserStoryId());
        delete("/us/" + userStory.getUserStoryId(), objectMapper.writeValueAsString(userStory)).andExpect(status().isOk()).andReturn();
    }


    private ResultActions get(String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(url))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions post(String url, String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions delete(String url, String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .delete(url)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }
}