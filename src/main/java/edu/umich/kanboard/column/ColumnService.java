package edu.umich.kanboard.column;

import edu.umich.kanboard.userstory.UserStoryEntity;
import edu.umich.kanboard.userstory.UserStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ColumnService {

    @Autowired
    private ColumnRepository columnRepository;

    @Autowired
    private UserStoryService userStoryService;

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

    public void deleteColumn(long columnId) {
        if (columnRepository.count() <= ColumnConstants.MIN_COLUMNS) {
            return;
        }

        List<ColumnEntity> columnEntities = new ArrayList<>();
        columnRepository.findAll().iterator().forEachRemaining(columnEntities::add);
        columnEntities.sort(Comparator.comparing(ColumnEntity::getId));

        ColumnEntity replacementColumn = columnEntities.get(0);

        if (replacementColumn.getId().equals(columnId)) {
            replacementColumn = columnEntities.get(1);
        }

        // Update user story column if we're deleting it
        for (UserStoryEntity userStory : userStoryService.getAllUserStories()) {
            if (userStory.getColumn().getId().equals(columnId)) {
                userStory.setColumn(replacementColumn);
                userStory.setPriority(userStoryService.getDefaultPriority(replacementColumn));
                userStoryService.saveUserStory(userStory);
            }
        }

        columnRepository.deleteById(columnId);
    }

    public ColumnEntity getDefaultColumnStatus() {
        List<ColumnEntity> columnEntities = new ArrayList<>();
        columnRepository.findAll().iterator().forEachRemaining(columnEntities::add);

        columnEntities.sort(Comparator.comparing(ColumnEntity::getId));
        return columnEntities.get(0);
    }

    public List<ColumnEntity> getAllColumns() {
        List<ColumnEntity> columns = new ArrayList<>();
        columnRepository.findAll().forEach(columns::add);
        return columns;
    }
}
