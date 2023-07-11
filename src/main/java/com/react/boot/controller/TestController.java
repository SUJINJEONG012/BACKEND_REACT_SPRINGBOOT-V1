package com.react.boot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.react.boot.dto.ResponseDTO;

@RestController
@RequestMapping("test")
public class TestController {
	
	@GetMapping
	public String testController() {
		return "Hello World";
	}
	// test/123
	@GetMapping("/{id}")
	public String testControllerWithPathVariables(@PathVariable(required=false)int id) {
		return "Hello World! " + id;
	}
	
	// test/testRequestParam?id=124
	@GetMapping("/testRequestParam")
	public String testControllerRequestParam(@RequestParam(required=false) int id) {
		return "Hello World! " + id;
	}
	
//	@GetMapping("/testRequestBody")
//	public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
//		return "Hello World ! ID : " 
//			+ testRequestBodyDTO.getId() 
//			+ "message : "
//			+ testRequestBodyDTO.getMessage();
//			}
	
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testControllerResponseEntity(){
		List<String>list = new ArrayList<>();
		list.add("Hello World ! I'm ResponseEntity. And you got 400!");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		//http status 400으로 설정
		return ResponseEntity.badRequest().body(response);
	}	
	
	
}
