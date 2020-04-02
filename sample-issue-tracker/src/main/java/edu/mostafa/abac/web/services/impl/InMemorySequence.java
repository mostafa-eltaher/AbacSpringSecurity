package edu.mostafa.abac.web.services.impl;

public class InMemorySequence {
	private Integer currentValue = 0;
	public Integer increment() {
		synchronized(this) {
			currentValue = currentValue + 1;
			return currentValue;
		}
	}
}
