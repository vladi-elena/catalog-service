package com.task.product.catalog.repository.category;

import com.task.product.catalog.domain.model.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Page<Category> findByParentId(UUID parentId, Pageable pageable);

    List<Category> findByParentIdIsNull();

    @Modifying
    @Query(value = "" +
            "INSERT INTO category_closure " +
            "            (ancestor_id," +
            "             descendant_id) " +
            "SELECT ancestor_id, " +
            "       :descendantId " +
            "FROM   category_closure " +
            "WHERE  descendant_id = :parentId " +
            "UNION ALL " +
            "SELECT :descendantId, " +
            "       :descendantId  ",
            nativeQuery = true)
    void saveDescendant(@Param("descendantId") UUID descendantId, @Param("parentId") UUID parentId);

    @Modifying
    @Query(value = "" +
            "INSERT INTO category_closure " +
            "            (ancestor_id," +
            "             descendant_id) " +
            "VALUES (:descendantId, " +
            "        :descendantId)",
            nativeQuery = true)
    void saveDescendant(@Param("descendantId") UUID descendantId);

}
