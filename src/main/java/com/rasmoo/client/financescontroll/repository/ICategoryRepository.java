package com.rasmoo.client.financescontroll.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rasmoo.client.financescontroll.entity.Category;

@Repository
public interface ICategoryRepository  extends JpaRepository<Category, Serializable>{

    @Query("SELECT c FROM Category c WHERE c.user.id = :userId")
    public List<Category> findAllByUserId(@Param("userId") final Long userId);

    @Query("SELECT c FROM Category c WHERE c.id = :categoryId AND c.user.id = :userId")
    public Optional<Category> findByUserId(@Param("categoryId") final Long categoryId, @Param("userId") final Long userId);

}
