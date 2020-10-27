package com.github.nemojmenervirat.flightadvisor.parsecsv;

public class ContextHolder<T, C extends ParseItemsContext<T>> {

	private C context;

	public C getContext() {
		return context;
	}

	public void setContext(C context) {
		this.context = context;
	}

}
