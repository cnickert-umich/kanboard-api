package edu.umich.kanboard.userstory;

import edu.umich.kanboard.column.ColumnEntity;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT MAX(us.priority) FROM UserStoryEntity us WHERE us.column = :column")
    Integer findHighestPriorityBasedOnColumn(ColumnEntity column);
}
