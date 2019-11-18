package edu.umich.kanboard.column;

import edu.umich.kanboard.userstory.UserStoryEntity;
import edu.umich.kanboard.userstory.UserStoryService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ColumnServiceTest {

    @InjectMocks
    ColumnService columnService;

    @Mock
    ColumnRepository columnRepository;

    @Mock
    UserStoryService userStoryService;

    @Test
    public void createNewColumn_success() {
        ColumnEntity expected = new ColumnEntity();
        expected.setName("DEFINED");

        when(columnRepository.save(expected)).thenReturn(expected);
        ColumnEntity actual = columnService.createOrUpdateColumn(expected);
        verify(columnRepository).save(expected);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void createNewColumn_nullName() {
        ColumnEntity col = new ColumnEntity();
        col.setName(null);

        ColumnEntity actual = columnService.createOrUpdateColumn(col);
        verify(columnRepository, times(0)).save(col);
        assertThat(actual).isNull();
    }

    @Test
    public void createNewColumn_emptyStringName() {
        ColumnEntity col = new ColumnEntity();
        col.setName("");

        ColumnEntity actual = columnService.createOrUpdateColumn(col);
        verify(columnRepository, times(0)).save(col);
        assertThat(actual).isNull();

    }

    @Test
    public void createNewColumn_tooManyColumns() {
        ColumnEntity newCol = new ColumnEntity();
        newCol.setName("yeet");

        when(columnRepository.count()).thenReturn((long) ColumnConstants.MAX_COLUMNS);

        ColumnEntity result = columnService.createOrUpdateColumn(newCol);
        verify(columnRepository, times(0)).save(newCol);

        assertThat(result).isNull();
    }

    @Test
    public void createNewColumn_nameTooLong() {
        ColumnEntity expected = new ColumnEntity();
        expected.setName(StringUtils.repeat("*", ColumnConstants.MAX_COLUMN_STRING_LENGTH + 1));


        ColumnEntity actual = columnService.createOrUpdateColumn(expected);
        verify(columnRepository, times(0)).save(expected);
        assertThat(actual).isNull();
    }

    @Test
    public void deleteColumn_noUserStories() {
        List<ColumnEntity> columns = new ArrayList<>();
        for (long i = 0; i < ColumnConstants.MAX_COLUMNS; i++) {
            columns.add(new ColumnEntity(i, "Garbage: " + i));
        }

        when(columnRepository.findAll()).thenReturn(columns);
        when(columnRepository.count()).thenReturn((long) columns.size());
        when(userStoryService.getAllUserStories()).thenReturn(Collections.emptyList());

        columnService.deleteColumn(0);
        verify(columnRepository).deleteById((long) 0);
    }

    @Test
    public void deleteColumn_userStoryWithDeletedColumn() {

        final int indexToDelete = (int) (ColumnConstants.MAX_COLUMNS * Math.random());

        // Create columns
        List<ColumnEntity> columns = new ArrayList<>();
        for (long i = 0; i < ColumnConstants.MAX_COLUMNS; i++) {
            columns.add(new ColumnEntity(i, "Garbage: " + i));
        }

        // Add user stories (one with column being deleted)
        List<UserStoryEntity> userStories = new ArrayList<>();
        for (int i = 0; i < ColumnConstants.MAX_COLUMNS; i++) {
            UserStoryEntity userStory = new UserStoryEntity();
            userStory.setColumn(columns.get(i));
            userStories.add(userStory);
        }

        ColumnEntity colToDelete = columns.get(indexToDelete);

        when(columnRepository.findAll()).thenReturn(columns);
        when(columnRepository.count()).thenReturn((long) columns.size());
        when(userStoryService.getAllUserStories()).thenReturn(userStories);

        columnService.deleteColumn(indexToDelete);
        verify(columnRepository).deleteById((long) indexToDelete);

        for (UserStoryEntity userStory : userStories) {
            assertThat(userStory.getColumn().getName()).isNotEqualTo(colToDelete.getName());
            assertThat(userStory.getColumn().getId()).isNotEqualTo(colToDelete.getId());
        }
    }

    @Test
    public void deleteColumn_failDoesNotMeetMinimumColumnRequirements() {
        ColumnEntity col = mock(ColumnEntity.class);
        long colIndexToDelete = 0;
        col.setId(colIndexToDelete);
        when(columnRepository.count()).thenReturn((long) ColumnConstants.MIN_COLUMNS);

        columnService.deleteColumn(colIndexToDelete);
        verify(columnRepository, times(0)).deleteById(colIndexToDelete);
    }

    @Test
    public void getAllColumns() {
        List<ColumnEntity> expected = new ArrayList<>();
        for (int i = 0; i < ColumnConstants.MAX_COLUMNS; i++) {
            expected.add(mock(ColumnEntity.class));
        }

        when(columnRepository.findAll()).thenReturn(expected);

        List<ColumnEntity> actual = columnService.getAllColumns();
        verify(columnRepository).findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getDefaultColumn() {
        List<ColumnEntity> entities = new ArrayList<>();
        for (long i = 0; i < ColumnConstants.MAX_COLUMNS; i++) {
            entities.add(new ColumnEntity(i, "LOOSE MOOSE"));
        }
        entities.add(new ColumnEntity((long) -1, "Blah blah blah"));

        ColumnEntity min = entities.get(entities.size() - 1);
        when(columnRepository.findAll()).thenReturn(entities);
        ColumnEntity actual = columnService.getDefaultColumnStatus();
        verify(columnRepository).findAll();
        assertThat(actual).isEqualTo(min);
    }
}
