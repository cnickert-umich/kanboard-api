package edu.umich.kanboard.column;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ColumnRepository extends CrudRepository<ColumnEntity, Long> {

    @Override
    Iterable<ColumnEntity> findAll();

    @Override
    long count();

    @Query("SELECT min(id) FROM column_headers")
    ColumnEntity getDefaultColumn();

    @Override
    <S extends ColumnEntity> S save(S entity);

    @Override
    <S extends ColumnEntity> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    void delete(ColumnEntity entity);
}
