package com.mostafa.security.services;

import java.util.List;

import com.mostafa.security.model.ToDoItem;

public interface ToDoService {
	public List<ToDoItem> getToDos();
	public ToDoItem getToDo(Long id);
	public void addToDo(ToDoItem item);
	public void updateToDo(ToDoItem item);
	public void deleteToDo(ToDoItem item);

}
