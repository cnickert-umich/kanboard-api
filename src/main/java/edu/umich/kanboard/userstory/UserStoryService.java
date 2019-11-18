package edu.umich.kanboard.userstory;

import edu.umich.kanboard.column.ColumnEntity;
import edu.umich.kanboard.column.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public UserStoryEntity saveUserStory(UserStoryEntity newOrExistingUserStory) {

        if (newOrExistingUserStory.getColumn() == null) {
            newOrExistingUserStory.setColumn(columnService.getDefaultColumnStatus());
        }

        if (newOrExistingUserStory.getName() == null || newOrExistingUserStory.getName().equals("")) {
            return null;
        }

        if (newOrExistingUserStory.getDescription() == null || newOrExistingUserStory.getDescription().equals("")) {
            return null;
        }

        if (newOrExistingUserStory.getPriority() == null) {
            newOrExistingUserStory.setPriority(this.getDefaultPriority(newOrExistingUserStory.getColumn()));
        }

        // Check if user story is new or not
        if (newOrExistingUserStory.getId() != null) {
            UserStoryEntity existingUserStory = userStoryRepository.findById(newOrExistingUserStory.getId()).get();

            // Check if column changed
            if (!existingUserStory.getColumn().equals(newOrExistingUserStory.getColumn())) {

                // Set priority of the user story with column change to highest
                newOrExistingUserStory.setPriority(this.getDefaultPriority(newOrExistingUserStory.getColumn()));

                // update user story priorities of old column
                List<UserStoryEntity> oldColumnUserStoriesWithHigherPriority = userStoryRepository.findAll().stream()
                        .filter(foundUserStory -> foundUserStory.getColumn().equals(existingUserStory.getColumn()))
                        .filter(foundUserStory -> foundUserStory.getPriority() > existingUserStory.getPriority())
                        .collect(Collectors.toList());

                // Drop the priority by one of every user story whose priority must change
                for (UserStoryEntity userStory : oldColumnUserStoriesWithHigherPriority) {
                    userStory.setPriority(userStory.getPriority() - 1);
                    userStoryRepository.save(userStory);
                }
            } else if (!existingUserStory.getPriority().equals(newOrExistingUserStory.getPriority())) {

                // Filter out a list of user stories
                List<UserStoryEntity> updatePriorityList = userStoryRepository.findAll().stream()
                        .filter(foundUserStory -> foundUserStory.getColumn().equals(newOrExistingUserStory.getColumn()))
                        .filter(foundUserStory -> (foundUserStory.getPriority() >= newOrExistingUserStory.getPriority() && foundUserStory.getPriority() < existingUserStory.getPriority()))
                        .collect(Collectors.toList());

                // Update the priority by one of every user story whose priority must change
                for (UserStoryEntity userStory : updatePriorityList) {
                    userStory.setPriority(userStory.getPriority() + 1);
                    userStoryRepository.save(userStory);
                }
            }
        }

        return userStoryRepository.save(newOrExistingUserStory);
    }

    public void deleteUserStory(Long id) {
        userStoryRepository.deleteById(id);
    }

    private int getDefaultPriority(ColumnEntity column) {
        Integer result = userStoryRepository.findHighestPriorityBasedOnColumn(column);
        if (result == null) {
            return 1;
        }

        return result + 1;
    }
}
