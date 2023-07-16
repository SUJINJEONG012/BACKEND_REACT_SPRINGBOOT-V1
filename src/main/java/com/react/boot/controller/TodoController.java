package com.react.boot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.boot.dto.ResponseDTO;
import com.react.boot.dto.TodoDTO;
import com.react.boot.entity.TodoEntity;
import com.react.boot.service.TodoService;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService todoService;
	
	@GetMapping("/test")
	public ResponseEntity<?> testTodo(){
		String str = todoService.testService(); //테스트 서비스 사용
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.ok().body(response);
	}
	
	@PostMapping
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto){
		
		try {
			String temporaryUserId = "temporary-user";
			
			// todoEntity로 변환
			TodoEntity entity = TodoDTO.todoEntity(dto);
			// id를 null로 초기화 생성, 생성 당시에는 id가 없어야 한다.
			entity.setId(null);
			/*
			 * 임시유저 아이디를 설정한다.  추후 인증과 인가에서 수정할 예쩡. 
			 * 지금은 인증과 인가기능이 없으므로 한 유저 (temporary-user)만 로그인 없이 사용 가능한 애플리케이션
			*/ 
			entity.setUserId(temporaryUserId);
			
			// 서비스를 이용해 todo 엔티티를 생성
			List<TodoEntity> entities = todoService.create(entity);
			
			// 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			//변환된  TodoDTO 리스트를 이용해 ResponseDTO을 초기화한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			//ResponseDTO 리턴
			return ResponseEntity.ok().body(response);
			
		}catch(Exception e) {
			//예외가 날 경우 dto대신 error에 메세지를 넣어 리턴한다.
			String error = e.getMessage();
			
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PutMapping
	public ResponseEntity<?> update(@RequestBody TodoDTO dto){
		String temporaryUserId = "temporary-user";
		// dto을 entity로 변환
		TodoEntity entity = TodoDTO.todoEntity(dto);
		//id를 temporaryUserId로 초기화. 
		entity.setUserId(temporaryUserId);
		//서비스를 이용해 entity를 업데이트
		List<TodoEntity> entities = todoService.update(entity);
		//자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환 
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		//변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		//ResponseDTO를 리턴
		
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto){
		try {
			String temporaryUserId = "temporary-user";
			//TodoEntity로 변환
			TodoEntity entity = TodoDTO.todoEntity(dto);
			//임시 유저 아이디를 설정. 
			entity.setUserId(temporaryUserId);
			// 서비스를 이용해 entity 삭제 
			List<TodoEntity> entities = todoService.delete(entity);
			// 자바스트럼을 이용해 리턴된 엔티티 리스트를 TodoList 리스트로 변환
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			//변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			return ResponseEntity.ok().body(response);
			
		}catch(Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	
}
