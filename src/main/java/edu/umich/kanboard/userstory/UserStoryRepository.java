package edu.umich.kanboard.userstory;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserStoryRepository extends CrudRepository<UserStoryEntity, Long> {

    List<UserStoryEntity> findAll();

    UserStoryEntity findByUserStoryId(Long userStoryId);

    List<UserStoryEntity> findTop8ByNameIgnoreCaseContainingOrderByName(String name);

    UserStoryEntity save(UserStoryEntity userStoryEntity);

}
