package edu.mostafa.abac.web.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import edu.mostafa.abac.web.model.ToDoItem;
import edu.mostafa.abac.web.services.ToDoService;

@Component
public class ToDoServiceImpl implements ToDoService {
	private List<ToDoItem> items = new ArrayList<ToDoItem>(5);
	
	public ToDoServiceImpl() {
		items.add(new ToDoItem(1l, "Item 1", "dev1"));
		items.add(new ToDoItem(2l, "Item 2", "dev2"));
		items.add(new ToDoItem(3l, "Item 3", "pm1"));
		items.add(new ToDoItem(4l, "Item 4", "pm2"));
		items.add(new ToDoItem(5l, "Item 5", "admin"));
	}

	@Override
	public List<ToDoItem> getToDos() {
		return items;
	}
	

	@Override
	public void addToDo(ToDoItem item) {
		items.add(item);
		
	}

	@Override
	public void updateToDo(ToDoItem item) {
		for(ToDoItem cItem : items) {
			if(cItem.getId().equals(item.getId())){
				cItem.setDesc(item.getDesc());
			}
		}
	}

	@Override
	public void deleteToDo(ToDoItem item) {
		ToDoItem existing = null;
		for(ToDoItem cItem : items) {
			if(cItem.getId().equals(item.getId())){
				existing = cItem;
			}
		}
		items.remove(existing);
	}

	@Override
	public ToDoItem getToDo(Long id) {
		for(ToDoItem cItem : items) {
			if(cItem.getId().equals(id)){
				return cItem;
			}
		}
		return null;
	}

}
