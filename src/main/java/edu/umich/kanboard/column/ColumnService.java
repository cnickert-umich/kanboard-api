package edu.umich.kanboard.column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ColumnService {

    @Autowired
    private ColumnRepository columnRepository;

    public ColumnEntity createOrUpdateColumn(ColumnEntity column) {

        if (column.getName() == null || column.getName().equals("")) {
            return null;
        }

        if (columnRepository.count() == ColumnConstants.MAX_COLUMNS) {
            return null;
        }

        if (column.getName().length() > ColumnConstants.MAX_COLUMN_STRING_LENGTH) {
            return null;
        }

        return columnRepository.save(column);
    }

    public void deleteColumn(ColumnEntity column) {

        if (columnRepository.count() > ColumnConstants.MIN_COLUMNS) {
            columnRepository.delete(column);
        }
    }

    public ColumnEntity getDefaultColumnStatus() {
        ColumnEntity min = null;
        Iterator<ColumnEntity> iterator = columnRepository.findAll().iterator();

        while (iterator.hasNext()) {
            ColumnEntity current = iterator.next();

            if (min == null) {
                min = iterator.next();
            }

            if (current.getId() < min.getId()) {
                min = current;
            }
        }

        return min;
    }

    public List<ColumnEntity> getAllColumns() {
        List<ColumnEntity> columns = new ArrayList<>();
        columnRepository.findAll().forEach(columns::add);
        return columns;
    }
}
