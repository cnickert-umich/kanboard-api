package edu.umich.kanboard.userstory;

import edu.umich.kanboard.column.ColumnEntity;
import edu.umich.kanboard.column.ColumnService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.Silent.class)
public class UserStoryServiceTest {

    private static final String US_NAME = "GOOSE";
    private static final String US_DESCRIPTION = "CANADIAN GEESE ARE ANNOYING";
    private static final String CN_NAME = "LOOSE";

    @InjectMocks
    UserStoryService userStoryService;

    @Mock
    UserStoryRepository userStoryRepository;

    @Mock
    ColumnService columnService;

    @Test
    public void getAllUserStories() {

        List<UserStoryEntity> expectedUserStories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            expectedUserStories.add(mock(UserStoryEntity.class));
        }

        when(userStoryRepository.findAll()).thenReturn(expectedUserStories);
        List<UserStoryEntity> actualUserStories = userStoryService.getAllUserStories();
        verify(userStoryRepository).findAll();

        assertThat(actualUserStories).isEqualTo(expectedUserStories);
    }

    @Test
    public void getUserStoryById() {
        final long USER_STORY_ID = 1;
        UserStoryEntity expected = new UserStoryEntity();
        expected.setId(USER_STORY_ID);

        when(userStoryRepository.findById(USER_STORY_ID)).thenReturn(Optional.of(expected));
        UserStoryEntity actual = userStoryService.getUserStoryById(USER_STORY_ID);
        verify(userStoryRepository).findById(USER_STORY_ID);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void createUserStory_happyPath() {
        UserStoryEntity expected = createUserStory();

        when(userStoryRepository.save(expected)).thenReturn(expected);
        UserStoryEntity actual = userStoryService.saveUserStory(expected);
        verify(userStoryRepository, times(1)).save(expected);

        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getColumn()).isEqualTo(expected.getColumn());
    }

    @Test
    public void createUserStory_columnMissingShouldAddDefaultColumn() {
        UserStoryEntity expected = createUserStory();
        expected.setColumn(null);

        ColumnEntity defaultEntity = new ColumnEntity();
        defaultEntity.setName(CN_NAME);

        ArgumentCaptor<UserStoryEntity> captor = ArgumentCaptor.forClass(UserStoryEntity.class);
        when(columnService.getDefaultColumnStatus()).thenReturn(defaultEntity);

        userStoryService.saveUserStory(expected);

        verify(columnService, times(1)).getDefaultColumnStatus();
        verify(userStoryRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getColumn().getName()).isEqualTo(defaultEntity.getName());
        assertThat(captor.getValue().getName()).isEqualTo(expected.getName());
        assertThat(captor.getValue().getDescription()).isEqualTo(expected.getDescription());

    }

    @Test
    public void createUserStory_nullName() {
        UserStoryEntity userStory = createUserStory();
        userStory.setName(null);

        UserStoryEntity result = userStoryService.saveUserStory(userStory);
        verify(userStoryRepository, times(0)).save(userStory);

        assertThat(result).isNull();

    }

    @Test
    public void createUserStory_emptyStringName() {
        UserStoryEntity userStory = createUserStory();
        userStory.setName("");

        UserStoryEntity result = userStoryService.saveUserStory(userStory);
        verify(userStoryRepository, times(0)).save(userStory);

        assertThat(result).isNull();
    }

    @Test
    public void createUserStory_nullDescription() {
        UserStoryEntity userStory = createUserStory();
        userStory.setDescription(null);

        UserStoryEntity result = userStoryService.saveUserStory(userStory);
        verify(userStoryRepository, times(0)).save(userStory);

        assertThat(result).isNull();

    }

    @Test
    public void createUserStory_emptyDescription() {
        UserStoryEntity userStory = createUserStory();
        userStory.setDescription("");

        UserStoryEntity result = userStoryService.saveUserStory(userStory);
        verify(userStoryRepository, times(0)).save(userStory);

        assertThat(result).isNull();

    }

    @Test
    public void deleteUserStory() {
        final long US_ID = 1;
        UserStoryEntity userStory = createUserStory();

        userStory.setId(US_ID);

        userStoryService.deleteUserStory(US_ID);
        verify(userStoryRepository, times(1)).deleteById(US_ID);
    }


    private UserStoryEntity createUserStory() {
        UserStoryEntity userStory = new UserStoryEntity();
        userStory.setName(US_NAME);
        userStory.setDescription(US_DESCRIPTION);

        ColumnEntity column = new ColumnEntity();
        column.setName(CN_NAME);
        userStory.setColumn(column);
        return userStory;
    }
}