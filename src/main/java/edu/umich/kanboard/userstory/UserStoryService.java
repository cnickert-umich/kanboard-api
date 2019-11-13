package edu.umich.kanboard.userstory;

import edu.umich.kanboard.column.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserStoryService {

    @Autowired
    private UserStoryRepository userStoryRepository;

    @Autowired
    private ColumnService columnService;

    public List<UserStoryEntity> getAllUserStories() {
        return userStoryRepository.findAll();
    }

    public UserStoryEntity getUserStoryById(long userStoryId) {
        return userStoryRepository.findById(userStoryId).orElse(null);
    }

    public UserStoryEntity saveUserStory(UserStoryEntity userStory) {

        if (userStory.getColumn() == null) {
            userStory.setColumn(columnService.getDefaultColumnStatus());
        }

        if (userStory.getName() == null || userStory.getName().equals("")) {
            return null;
        }

        if (userStory.getDescription() == null || userStory.getDescription().equals("")) {
            return null;
        }

        return userStoryRepository.save(userStory);
    }

    public void deleteUserStory(Long id) {
        userStoryRepository.deleteById(id);
    }
}
