package edu.umich.kanboard.column;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
        ColumnEntity actual = columnService.save(expected);
        verify(columnRepository).save(expected);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void createNewColumn_nullName() {
        ColumnEntity col = new ColumnEntity();
        col.setName(null);

        ColumnEntity actual = columnService.save(col);
        verify(columnRepository, times(0)).save(col);
        assertThat(actual).isNull();
    }

    @Test
    public void createNewColumn_emptyStringName() {
        ColumnEntity col = new ColumnEntity();
        col.setName("");

        ColumnEntity actual = columnService.save(col);
        verify(columnRepository, times(0)).save(col);
        assertThat(actual).isNull();

    }

    @Test
    public void createNewColumn_tooManyColumns() {
        ColumnEntity newCol = new ColumnEntity();
        newCol.setName("yeet");

        when(columnRepository.count()).thenReturn((long) ColumnConstants.MAX_COLUMNS);

        ColumnEntity result = columnService.save(newCol);
        verify(columnRepository, times(0)).save(newCol);

        assertThat(result).isNull();
    }

    @Test
    public void createNewColumn_nameTooLong() {
        ColumnEntity expected = new ColumnEntity();
        expected.setName(StringUtils.repeat("*", ColumnConstants.MAX_COLUMN_STRING_LENGTH + 1));


        ColumnEntity actual = columnService.save(expected);
        verify(columnRepository, times(0)).save(expected);
        assertThat(actual).isNull();
    }
}
