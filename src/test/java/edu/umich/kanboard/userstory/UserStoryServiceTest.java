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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.Silent.class)
public class UserStoryServiceTest {

    private static final String US_NAME = "GOOSE";
    private static final String US_DESCRIPTION = "CANADIAN GEESE ARE ANNOYING";
    private static final String CN_NAME = "LOOSE";
    private static final Integer PRIORITY = 1;
    private static final String COLUMN_NAME = "COLUMN";

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
    public void updateUserStory_updatedPriority_changesStoryPriorityLowerInCurrentColumn() {
        final Integer NEW_PRIORITY = 1;

        List<UserStoryEntity> userStoryList = new ArrayList<>();
        ColumnEntity col = new ColumnEntity((long) 1, COLUMN_NAME);

        //Create user stories in same column
        for (int i = 1; i <= 5; i++) {
            UserStoryEntity userStory = createUserStory(i);
            userStory.setId((long) i);
            userStory.setColumn(col);
            when(userStoryRepository.findById((long) i)).thenReturn(Optional.of(userStory));
            userStoryList.add(userStory);
        }

        // Copy the last user story with updated priority
        UserStoryEntity updatedUserStory = createUserStory(userStoryList.size());
        updatedUserStory.setId((long) userStoryList.size());
        updatedUserStory.setPriority(NEW_PRIORITY);
        updatedUserStory.setColumn(col);

        ArgumentCaptor<UserStoryEntity> captor = ArgumentCaptor.forClass(UserStoryEntity.class);
        when(userStoryRepository.findHighestPriorityBasedOnColumn(col)).thenReturn(NEW_PRIORITY);
        when(userStoryRepository.findAll()).thenReturn(userStoryList);

        // Update the user story
        userStoryService.saveUserStory(updatedUserStory);

        // Verify the changes
        verify(userStoryRepository, times(userStoryList.size())).save(captor.capture());

        // Sort the captured saved user stories
        List<UserStoryEntity> savedUserStories = captor.getAllValues();
        savedUserStories.sort(Comparator.comparing(UserStoryEntity::getPriority));

        // Check the priorities are updated
        for (int i = 1; i <= savedUserStories.size(); i++) {
            assertThat(savedUserStories.get(i - 1).getPriority()).isEqualTo(i);
        }
    }

    @Test(expected = UserStoryExceptions.UserStoryBadPriorityException.class)
    public void updateUserStory_updatedPriority_tooLow() {
        final Integer NEW_PRIORITY = 0;

        UserStoryEntity userStoryEntity = createUserStory();
        userStoryEntity.setPriority(NEW_PRIORITY);


        UserStoryEntity actual = userStoryService.saveUserStory(userStoryEntity);

        assertThat(actual).isNull();
        verify(userStoryRepository, times(0)).save(actual);

    }

    @Test(expected = UserStoryExceptions.UserStoryBadPriorityException.class)
    public void updateUserStory_updatedPriority_tooHighExistingUserStory() {
        final Integer NEW_PRIORITY = 2;

        UserStoryEntity userStoryEntity = createUserStory();
        userStoryEntity.setPriority(NEW_PRIORITY);
        userStoryEntity.setId((long) 1);

        when(userStoryRepository.findHighestPriorityBasedOnColumn(userStoryEntity.getColumn())).thenReturn(NEW_PRIORITY - 1);
        UserStoryEntity actual = userStoryService.saveUserStory(userStoryEntity);

        assertThat(actual).isNull();
        verify(userStoryRepository, times(0)).save(actual);
    }

    @Test
    public void updateUserStory_updatedPriority_tooHighSetsPriority() {
        final Integer NEW_PRIORITY = 3;

        UserStoryEntity userStoryEntity = createUserStory();
        userStoryEntity.setPriority(NEW_PRIORITY);
        userStoryEntity.setId(null);

        when(userStoryRepository.findHighestPriorityBasedOnColumn(userStoryEntity.getColumn())).thenReturn(NEW_PRIORITY - 1);
        when(userStoryRepository.save(userStoryEntity)).thenReturn(userStoryEntity);
        UserStoryEntity actual = userStoryService.saveUserStory(userStoryEntity);

        assertThat(actual).isEqualTo(userStoryEntity);
        verify(userStoryRepository, times(1)).save(actual);
    }

    @Test
    public void updateUserStory_updatedPriority_changesStoryPriorityHigherInCurrentColumn() {
        final Integer NEW_PRIORITY = 5;

        List<UserStoryEntity> userStoryList = new ArrayList<>();
        ColumnEntity col = new ColumnEntity((long) 1, COLUMN_NAME);

        //Create user stories in same column
        for (int i = 1; i <= 5; i++) {
            UserStoryEntity userStory = createUserStory(i);
            userStory.setId((long) i);
            userStory.setColumn(col);
            when(userStoryRepository.findById((long) i)).thenReturn(Optional.of(userStory));
            userStoryList.add(userStory);
        }

        // Copy the last user story with updated priority
        UserStoryEntity updatedUserStory = createUserStory(1);
        updatedUserStory.setId((long) 1);
        updatedUserStory.setPriority(NEW_PRIORITY);
        updatedUserStory.setColumn(col);

        ArgumentCaptor<UserStoryEntity> captor = ArgumentCaptor.forClass(UserStoryEntity.class);

        when(userStoryRepository.findHighestPriorityBasedOnColumn(col)).thenReturn(NEW_PRIORITY);
        when(userStoryRepository.findAll()).thenReturn(userStoryList);

        // Update the user story
        userStoryService.saveUserStory(updatedUserStory);

        // Verify the changes
        verify(userStoryRepository, times(userStoryList.size())).save(captor.capture());

        // Sort the captured saved user stories
        List<UserStoryEntity> savedUserStories = captor.getAllValues();
        savedUserStories.sort(Comparator.comparing(UserStoryEntity::getPriority));

        // Check the priorities are updated
        for (int i = 1; i <= savedUserStories.size(); i++) {
            assertThat(savedUserStories.get(i - 1).getPriority()).isEqualTo(i);
        }
    }


    // This test is partially hardcoded based on the number of elements.
    // It's a hard case to test, so leaving it as it covers our use case + coverage
    @Test
    public void updateUserStory_updatedColumn_changesStoryPriorityAndOldStoryPriorities() {
        final Integer NEW_HIGHEST_PRIORITY = 5;

        //Create list of user stories
        List<UserStoryEntity> userStoryList = new ArrayList<>();

        // Create the columns
        ColumnEntity col1 = new ColumnEntity((long) 1, "First Column");
        ColumnEntity col2 = new ColumnEntity((long) 2, "Second Column");

        //Create user stories in first column
        for (int i = 1; i <= 5; i++) {
            UserStoryEntity userStory = createUserStory();
            userStory.setId((long) i);
            userStory.setPriority(i);
            userStory.setColumn(col1);
            when(userStoryRepository.findById((long) i)).thenReturn(Optional.of(userStory));
            userStoryList.add(userStory);
        }

        // Make copy of the first user story and update column
        UserStoryEntity updatedUserStory = createUserStory();
        updatedUserStory.setId((long) 1);
        updatedUserStory.setPriority(1);
        updatedUserStory.setColumn(col2);

        when(userStoryRepository.findAll()).thenReturn(userStoryList);
        when(userStoryRepository.findHighestPriorityBasedOnColumn(col2)).thenReturn(NEW_HIGHEST_PRIORITY);

        // Save the updated story
        userStoryService.saveUserStory(updatedUserStory);

        // Capture all the newly saved user stories based on priority changes
        ArgumentCaptor<UserStoryEntity> captor = ArgumentCaptor.forClass(UserStoryEntity.class);
        verify(userStoryRepository, times(userStoryList.size())).save(captor.capture());

        List<UserStoryEntity> savedUserStories = captor.getAllValues();

        // Separate out captures into their appropriate lists
        List<UserStoryEntity> userStoryColumnOneList = savedUserStories.stream()
                .filter(story -> story.getColumn().equals(col1))
                .collect(Collectors.toList());

        List<UserStoryEntity> userStoryColumnTwoList = savedUserStories.stream()
                .filter(story -> story.getColumn().equals(col2))
                .collect(Collectors.toList());

        userStoryColumnOneList.sort(Comparator.comparing(UserStoryEntity::getPriority));

        // Assert that the updated story is in column two with it's corresponding priority
        assertThat(userStoryColumnTwoList.size()).isEqualTo(1);
        assertThat(userStoryColumnTwoList.get(0).getPriority()).isEqualTo(NEW_HIGHEST_PRIORITY + 1);

        // Assert that the stories are in priority order in column one now
        for (int i = 1; i <= userStoryColumnOneList.size(); i++) {
            assertThat(userStoryColumnOneList.get(i - 1).getPriority()).isEqualTo(i);
        }
    }

    @Test
    public void createUserStory_ultimateHappyPath() {
        UserStoryEntity expected = createUserStory();
        expected.setPriority(PRIORITY);

        when(userStoryRepository.save(expected)).thenReturn(expected);
        UserStoryEntity actual = userStoryService.saveUserStory(expected);
        verify(userStoryRepository, times(1)).save(expected);

        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getColumn()).isEqualTo(expected.getColumn());
        assertThat(actual.getPriority()).isEqualTo(PRIORITY);
    }

    @Test
    public void createUserStory_happyPath_MissingPriority() {
        final Integer PRIORITY = 1;
        final Integer NEW_PRIORITY = PRIORITY + 1;

        UserStoryEntity expected = createUserStory();
        expected.setPriority(null);

        ArgumentCaptor<UserStoryEntity> captor = ArgumentCaptor.forClass(UserStoryEntity.class);
        when(userStoryRepository.findHighestPriorityBasedOnColumn(expected.getColumn())).thenReturn(PRIORITY);

        userStoryService.saveUserStory(expected);

        verify(userStoryRepository, times(2)).findHighestPriorityBasedOnColumn(expected.getColumn());
        verify(userStoryRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getColumn().getName()).isEqualTo(expected.getColumn().getName());
        assertThat(captor.getValue().getName()).isEqualTo(expected.getName());
        assertThat(captor.getValue().getDescription()).isEqualTo(expected.getDescription());
        assertThat(captor.getValue().getPriority()).isEqualTo(NEW_PRIORITY);
    }


    @Test
    public void createUserStory_happyPath_missingColumn() {
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

    @Test(expected = UserStoryExceptions.UserStoryInvalidNameException.class)
    public void createUserStory_badPath_nullName() {
        UserStoryEntity userStory = createUserStory();
        userStory.setName(null);

        UserStoryEntity result = userStoryService.saveUserStory(userStory);
        verify(userStoryRepository, times(0)).save(userStory);

        assertThat(result).isNull();

    }

    @Test(expected = UserStoryExceptions.UserStoryInvalidNameException.class)
    public void createUserStory_badPath_emptyStringName() {
        UserStoryEntity userStory = createUserStory();
        userStory.setName("");

        UserStoryEntity result = userStoryService.saveUserStory(userStory);
        verify(userStoryRepository, times(0)).save(userStory);

        assertThat(result).isNull();
    }

    @Test(expected = UserStoryExceptions.UserStoryInvalidDescriptionException.class)
    public void createUserStory_badPath_nullDescription() {
        UserStoryEntity userStory = createUserStory();
        userStory.setDescription(null);

        UserStoryEntity result = userStoryService.saveUserStory(userStory);
        verify(userStoryRepository, times(0)).save(userStory);

        assertThat(result).isNull();

    }

    @Test(expected = UserStoryExceptions.UserStoryInvalidDescriptionException.class)
    public void createUserStory_badPath_emptyDescription() {
        UserStoryEntity userStory = createUserStory();
        userStory.setDescription("");

        UserStoryEntity result = userStoryService.saveUserStory(userStory);
        verify(userStoryRepository, times(0)).save(userStory);

        assertThat(result).isNull();

    }

    @Test(expected = UserStoryExceptions.UserStoryNotFound.class)
    public void deleteUserStory_invalidId() {
        final long US_ID = 1;

        UserStoryEntity userStory = createUserStory();
        userStory.setId(US_ID);

        when(userStoryRepository.findById(US_ID)).thenReturn(Optional.empty());
        userStoryService.deleteUserStory(US_ID);
        verify(userStoryRepository, times(0)).deleteById(US_ID);
    }

    @Test
    public void deleteUserStory_happyPath() {
        final long US_ID = 7;
        final int PRIORITY = 1;

        UserStoryEntity userStoryToDelete = createUserStory();
        userStoryToDelete.setId(US_ID);
        userStoryToDelete.setPriority(PRIORITY);

        List<UserStoryEntity> userStories = new ArrayList<>();

        for (int i = 2; i < 7; i++) {
            userStories.add(createUserStory(i));
        }

        when(userStoryRepository.findById(US_ID)).thenReturn(Optional.of(userStoryToDelete));
        when(userStoryRepository.findAll()).thenReturn(userStories);

        List<UserStoryEntity> updatePriorityList = userStories.stream()
                .filter(userStory -> userStory.getPriority() > PRIORITY)
                .collect(Collectors.toList());

        ArgumentCaptor<UserStoryEntity> captor = ArgumentCaptor.forClass(UserStoryEntity.class);
        userStoryService.deleteUserStory(US_ID);

        verify(userStoryRepository, times(updatePriorityList.size())).save(captor.capture());

        List<UserStoryEntity> actual = captor.getAllValues();
        actual.sort(Comparator.comparing(UserStoryEntity::getPriority));

        for (int i = 1; i <= actual.size(); i++) {
            assertThat(actual.get(i - 1).getPriority()).isEqualTo(i);
        }
    }

    @Test
    public void getDefaultPriorityBasedOnColumn() {
        final int PRIORITY = 1;
        ColumnEntity col = mock(ColumnEntity.class);

        when(userStoryRepository.findHighestPriorityBasedOnColumn(col)).thenReturn(PRIORITY);
        int defaultPriority = userStoryService.getDefaultPriority(col);

        assertThat(defaultPriority).isEqualTo(PRIORITY + 1);
        verify(userStoryRepository, times(1)).findHighestPriorityBasedOnColumn(col);

    }

    @Test
    public void getDefaultPriorityBasedOnColumn_NoUserStoriesInColumnReturnsPriorityOf1() {
        ColumnEntity col = mock(ColumnEntity.class);

        when(userStoryRepository.findHighestPriorityBasedOnColumn(col)).thenReturn(null);
        int defaultPriority = userStoryService.getDefaultPriority(col);

        assertThat(defaultPriority).isEqualTo(1);
        verify(userStoryRepository, times(1)).findHighestPriorityBasedOnColumn(col);

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

    private UserStoryEntity createUserStory(int i) {
        UserStoryEntity userStory = new UserStoryEntity();
        userStory.setName(US_NAME + " " + i);
        userStory.setDescription(US_DESCRIPTION + " " + i);
        userStory.setPriority(i);

        ColumnEntity column = new ColumnEntity();
        column.setName(CN_NAME);
        userStory.setColumn(column);
        return userStory;
    }
}