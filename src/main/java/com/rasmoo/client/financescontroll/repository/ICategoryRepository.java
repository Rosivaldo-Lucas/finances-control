package com.rasmoo.client.financescontroll.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rasmoo.client.financescontroll.entity.Category;

@Repository
public interface ICategoryRepository  extends JpaRepository<Category, Serializable>{

}
