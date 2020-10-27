package com.github.nemojmenervirat.flightadvisor.parsecsv;

public interface ParseProcessor<T, C extends ParseItemsContext<T>> {

	public C processStarted();

	void rowProcessed(String[] row, C context);

	void processEnded(C context);

}
