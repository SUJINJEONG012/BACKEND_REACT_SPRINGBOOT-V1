package com.react.boot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.react.boot.entity.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
	
	List<TodoEntity> findByUserId(String userId);

	@Query("select t from TodoEntity t where t.userId= ?1")
	TodoEntity findByUserIdQuery(String userId);
}
