package edu.umich.kanboard.userstory;

import edu.umich.kanboard.column.ColumnEntity;
import edu.umich.kanboard.column.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public UserStoryEntity saveUserStory(UserStoryEntity userStoryToSave) {

        if (userStoryToSave.getColumn() == null) {
            userStoryToSave.setColumn(columnService.getDefaultColumnStatus());
        }

        if (userStoryToSave.getName() == null || userStoryToSave.getName().equals("")) {
            throw new UserStoryExceptions.UserStoryInvalidNameException(userStoryToSave.getName());
        }

        if (userStoryToSave.getDescription() == null || userStoryToSave.getDescription().equals("")) {
            throw new UserStoryExceptions.UserStoryInvalidDescriptionException(userStoryToSave.getDescription());
        }

        if (userStoryToSave.getPriority() == null) {
            userStoryToSave.setPriority(this.getDefaultPriority(userStoryToSave.getColumn()));
        }

        if (userStoryToSave.getPriority() <= 0) {
            // Tried to set priority too low
            throw new UserStoryExceptions.UserStoryBadPriorityException(userStoryToSave.getPriority());
        }

        // Check if user story is not new
        if (userStoryToSave.getId() != null) {

            if (userStoryToSave.getPriority() >= getDefaultPriority(userStoryToSave.getColumn())) {
                // Tried to set priority too high
                throw new UserStoryExceptions.UserStoryBadPriorityException(userStoryToSave.getPriority());
            }

            UserStoryEntity existingUserStory = userStoryRepository.findById(userStoryToSave.getId()).get();

            // Check if column changed
            if (!existingUserStory.getColumn().equals(userStoryToSave.getColumn())) {

                // Set priority of the user story with column change to highest
                userStoryToSave.setPriority(this.getDefaultPriority(userStoryToSave.getColumn()));

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
            } else if (!existingUserStory.getPriority().equals(userStoryToSave.getPriority())) {

                List<UserStoryEntity> updatePriorityList;

                // Filter out a list of user stories
                if (userStoryToSave.getPriority() > existingUserStory.getPriority()) {
                    updatePriorityList = userStoryRepository.findAll().stream()
                            .filter(foundUserStory -> foundUserStory.getColumn().equals(userStoryToSave.getColumn()))
                            .filter(foundUserStory -> (foundUserStory.getPriority() > existingUserStory.getPriority() && foundUserStory.getPriority() <= userStoryToSave.getPriority()))
                            .collect(Collectors.toList());

                    // Update the priority by one of every user story whose priority must change
                    for (UserStoryEntity userStory : updatePriorityList) {
                        userStory.setPriority(userStory.getPriority() - 1);
                        userStoryRepository.save(userStory);
                    }

                } else {
                    updatePriorityList = userStoryRepository.findAll().stream()
                            .filter(foundUserStory -> foundUserStory.getColumn().equals(userStoryToSave.getColumn()))
                            .filter(foundUserStory -> (foundUserStory.getPriority() >= userStoryToSave.getPriority() && foundUserStory.getPriority() < existingUserStory.getPriority()))
                            .collect(Collectors.toList());

                    // Update the priority by one of every user story whose priority must change
                    for (UserStoryEntity userStory : updatePriorityList) {
                        userStory.setPriority(userStory.getPriority() + 1);
                        userStoryRepository.save(userStory);
                    }
                }
            }
        } else if (userStoryToSave.getPriority() > getDefaultPriority(userStoryToSave.getColumn())) {
            // Tried to set priority too high
            throw new UserStoryExceptions.UserStoryBadPriorityException(userStoryToSave.getPriority());
        }

        return userStoryRepository.save(userStoryToSave);
    }

    public void deleteUserStory(Long id) {
        Optional<UserStoryEntity> userStoryToDelete = userStoryRepository.findById(id);
        if (!userStoryToDelete.isPresent()) {
            throw new UserStoryExceptions.UserStoryNotFound();
        }

        List<UserStoryEntity> updateUserStories = userStoryRepository.findAll().stream()
                .filter(userStory -> userStory.getPriority() > userStoryToDelete.get().getPriority())
                .collect(Collectors.toList());

        for (UserStoryEntity userStoryEntity : updateUserStories) {
            userStoryEntity.setPriority(userStoryEntity.getPriority() - 1);
            userStoryRepository.save(userStoryEntity);
        }

        userStoryRepository.deleteById(id);

    }

    public int getDefaultPriority(ColumnEntity column) {
        Integer result = userStoryRepository.findHighestPriorityBasedOnColumn(column);
        if (result == null) {
            return 1;
        }

        return result + 1;
    }
}
