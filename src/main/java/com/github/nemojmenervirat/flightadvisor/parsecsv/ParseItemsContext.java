package com.github.nemojmenervirat.flightadvisor.parsecsv;

import java.util.LinkedList;
import java.util.List;

public class ParseItemsContext<T> {

	private List<T> resultList = new LinkedList<>();
	private int ignored;

	public void addResult(T t) {
		resultList.add(t);
	}

	public List<T> getResultList() {
		return resultList;
	}

	public void ignoreRow() {
		ignored++;
	}

	public int getImportedCount() {
		return resultList.size();
	}

	public int getIgnoredCount() {
		return ignored;
	}

}
