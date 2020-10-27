package com.github.nemojmenervirat.flightadvisor.parsecsv;

public class ParseItemsResult {

	private int imported;
	private int ignored;

	public static ParseItemsResult of(int imported, int ignored) {
		return new ParseItemsResult(imported, ignored);
	}

	private ParseItemsResult(int imported, int ignored) {
		this.imported = imported;
		this.ignored = ignored;
	}

	public int getImported() {
		return imported;
	}

	public int getIgnored() {
		return ignored;
	}

}
