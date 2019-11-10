package edu.umich.kanboard.column;

import org.springframework.data.repository.CrudRepository;

public interface ColumnRepository extends CrudRepository<ColumnEntity, Long> {

    @Override
    Iterable<ColumnEntity> findAll();

    @Override
    long count();

    @Override
    <S extends ColumnEntity> S save(S entity);

    @Override
    void delete(ColumnEntity entity);
}
