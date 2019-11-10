package edu.umich.kanboard.column;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ColumnServiceTest {

    @InjectMocks
    ColumnService columnService;

    @Mock
    ColumnRepository columnRepository;

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
    public void deleteColumn_happyPath() {
        ColumnEntity col = mock(ColumnEntity.class);
        when(columnRepository.count()).thenReturn((long) ColumnConstants.MIN_COLUMNS + 1);


        columnService.deleteColumn(col);
        verify(columnRepository).delete(col);
    }

    @Test
    public void deleteColumn_failDoesNotMeetMinimumColumnRequirements() {
        ColumnEntity col = mock(ColumnEntity.class);
        when(columnRepository.count()).thenReturn((long) ColumnConstants.MIN_COLUMNS);

        columnService.deleteColumn(col);
        verify(columnRepository, times(0)).delete(col);
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
