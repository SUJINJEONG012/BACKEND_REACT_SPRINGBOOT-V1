package com.react.boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.boot.entity.TodoEntity;
import com.react.boot.repository.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {
	
	@Autowired
	private TodoRepository todoRepository;
	
	
	public String testService() {
		TodoEntity entity = TodoEntity.builder().title("My Firest todo item").build();
		todoRepository.save(entity);
		TodoEntity savedEntity = todoRepository.findById(entity.getId()).get();
		return savedEntity.getTitle();
	}
	
	public List<TodoEntity> create(final TodoEntity entity){
		// Validations
	    validate(entity);
	    todoRepository.save(entity);
	    log.info("Entity Id : {} is saved.", entity.getId());
	    return todoRepository.findByUserId(entity.getUserId());
	}
	
	// 리펙토링 한 메서드
	private void validate(final TodoEntity entity) {
		if(entity == null) {
			log.warn("Entity cannot be null");
			throw new RuntimeException("Entity cannot be null");
		}
		if(entity.getUserId() == null) {
			log.warn("Unknow user.");
			throw new RuntimeException("Unknow user.");
		}
	}
	
	public List<TodoEntity> retrieve(final String userId) {
	    return todoRepository.findByUserId(userId);
	  }
	
	public List<TodoEntity> update(final TodoEntity entity){
		//저장할 엔티티가 유효한지 확인. 
		validate(entity);
		//넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트 할 수 없다.
		final Optional<TodoEntity> original = todoRepository.findById(entity.getId());
		
		if(original.isPresent()) {
			final TodoEntity todo = original.get();
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
		}
		
		//람다식
//		original.ifPresent(todo -> {
//			//반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어씌운다.
//			todo.setTitle(entity.getTitle());
//			todo.setDone(entity.isDone());
//			
//			//데이터베이스 새 값을 저장
//			todoRepository.save(todo);
//		});
		
		//Retrieve Todo에서 만든 메서드를 이용해 유저의 모든 Todo 리스트를 리턴한다.
		return retrieve(entity.getUserId());
		
	}
	
	
	public List<TodoEntity> delete(final TodoEntity entity){
		// 저장할 엔티티가 유효한지 확인
		validate(entity);
		
		try {
			//엔티티를 삭제
			todoRepository.delete(entity);
			
		}catch(Exception e) {
			//exception 발생 시 id와 exception을 로깅한다.
			log.error("error deleting entity : ", entity.getId(), e);
			//컨트롤러로 exception을 날린다. 데이터베이스 내부로직을 캡슐화 하기위해 e를 리턴하지 않고 새 exception 오브젝트를 리턴
			throw new RuntimeException("error deleting entity :" + entity.getId());
		}
		return retrieve(entity.getUserId());
	}
	
	
	
	

	
	
	
}
