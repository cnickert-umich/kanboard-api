package edu.umich.kanboard.userstory;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserStoryRepository extends CrudRepository<UserStoryEntity, Long> {

    @Override
    List<UserStoryEntity> findAll();

    @Override
    Optional<UserStoryEntity> findById(Long id);

    @Override
    <S extends UserStoryEntity> S save(S entity);

    @Override
    void deleteById(Long id);
}
