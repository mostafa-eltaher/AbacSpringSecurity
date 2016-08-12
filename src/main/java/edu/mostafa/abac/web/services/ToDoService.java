package edu.mostafa.abac.web.services;

import java.util.List;

import edu.mostafa.abac.web.model.ToDoItem;

public interface ToDoService {
	public List<ToDoItem> getToDos();
	public ToDoItem getToDo(Long id);
	public void addToDo(ToDoItem item);
	public void updateToDo(ToDoItem item);
	public void deleteToDo(ToDoItem item);

}
