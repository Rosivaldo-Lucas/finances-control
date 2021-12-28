package com.rasmoo.client.financescontroll.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rasmoo.client.financescontroll.entity.Entry;

@Repository
public interface IEntryRepository  extends JpaRepository<Entry, Serializable>{

}
