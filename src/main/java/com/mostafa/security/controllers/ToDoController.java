package com.mostafa.security.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mostafa.security.model.ToDoItem;
import com.mostafa.security.services.ToDoService;

@RestController
@RequestMapping("/todo")
public class ToDoController {
	private static final Logger log = LoggerFactory.getLogger(ToDoController.class);
	
	@Autowired
	ToDoService toDoService;
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ToDoItem> getToDos() {
		log.info("[getToDos] started ...");
		List<ToDoItem> result = toDoService.getToDos();
		log.info("[getToDos] done, {} items.", result == null? 0 : result.size());
		return result;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@PostAuthorize("hasPermission(returnObject,'VIEW_TODO')")
	public ToDoItem getToDo(@PathVariable("id") Long id) {
		log.info("[getToDo] started ...");
		ToDoItem result = toDoService.getToDo(id);
		log.info("[getToDo] done,  result: " + result);
		return result;
	}
}
