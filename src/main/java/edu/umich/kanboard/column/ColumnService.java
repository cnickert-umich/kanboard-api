package edu.umich.kanboard.column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnService {

    @Autowired
    private ColumnRepository columnRepository;

    public ColumnEntity save(ColumnEntity column) {

        if (column.getName() == null || column.getName().equals("")) {
            return null;
        }

        if (columnRepository.count() == ColumnConstants.MAX_COLUMNS) {
            return null;
        }

        if(column.getName().length() > ColumnConstants.MAX_COLUMN_STRING_LENGTH) {
            return null;
        }

        return columnRepository.save(column);
    }
}