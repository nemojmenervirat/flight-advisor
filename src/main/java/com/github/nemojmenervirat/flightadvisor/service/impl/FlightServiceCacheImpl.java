package com.github.nemojmenervirat.flightadvisor.service.impl;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.nemojmenervirat.flightadvisor.payload.FlightResponse;
import com.github.nemojmenervirat.flightadvisor.service.FlightServiceCache;

@Component
class FlightServiceCacheImpl implements FlightServiceCache {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final int CACHE_SIZE = 100;
	private static final int EXPIRATION_TIME_MINUTES = 30;

	private List<FlightCacheItem> flightCacheItems = new LinkedList<>();

	public synchronized FlightResponse getFlightResponse(long sourceCityId, long destinationCityId) {
		log.debug("Searching cache for {" + sourceCityId + ", " + destinationCityId + "}");
		FlightCacheItem flightCacheItem = flightCacheItems.stream()
				.filter(item -> item.getSourceCityId() == sourceCityId
						&& item.getDestinationCityId() == destinationCityId
						&& item.getModified().isAfter(LocalDateTime.now().minusMinutes(EXPIRATION_TIME_MINUTES)))
				.findAny().orElse(null);

		if (flightCacheItem != null) {
			log.debug("Found in cache");
			flightCacheItem.setModified(LocalDateTime.now());
			flightCacheItem.setUsages(flightCacheItem.getUsages() + 1);
			return flightCacheItem.getFlightResponse();
		}
		log.debug("Not found in cache");
		return null;
	}

	public synchronized void addFlightResponse(long sourceCityId, long destinationCityId, FlightResponse flightResponse) {
		log.debug("Adding to cache {" + sourceCityId + ", " + destinationCityId + ", " + flightResponse.getPrice() + "}");
		if (flightCacheItems.size() >= CACHE_SIZE) {
			boolean removed = flightCacheItems.removeIf(item -> item.getModified().isBefore(LocalDateTime.now().minusMinutes(EXPIRATION_TIME_MINUTES)));
			if (!removed) {
				FlightCacheItem forRemoval = flightCacheItems.stream().min((item1, item2) -> Integer.compare(item1.getUsages(), item2.getUsages()))
						.orElse(flightCacheItems.get(0));
				flightCacheItems.remove(forRemoval);
			}
		}
		FlightCacheItem flightCacheItem = new FlightCacheItem();
		flightCacheItem.setSourceCityId(sourceCityId);
		flightCacheItem.setDestinationCityId(destinationCityId);
		flightCacheItem.setFlightResponse(flightResponse);
		flightCacheItem.setModified(LocalDateTime.now());
		flightCacheItem.setUsages(1);
		flightCacheItems.add(flightCacheItem);
	}

	class FlightCacheItem {
		private long sourceCityId;
		private long destinationCityId;
		private FlightResponse flightResponse;
		private int usages;
		private LocalDateTime modified;

		public long getSourceCityId() {
			return sourceCityId;
		}

		public void setSourceCityId(long sourceCityId) {
			this.sourceCityId = sourceCityId;
		}

		public long getDestinationCityId() {
			return destinationCityId;
		}

		public void setDestinationCityId(long destinationCityId) {
			this.destinationCityId = destinationCityId;
		}

		public FlightResponse getFlightResponse() {
			return flightResponse;
		}

		public void setFlightResponse(FlightResponse flightResponse) {
			this.flightResponse = flightResponse;
		}

		public int getUsages() {
			return usages;
		}

		public void setUsages(int usages) {
			this.usages = usages;
		}

		public LocalDateTime getModified() {
			return modified;
		}

		public void setModified(LocalDateTime modified) {
			this.modified = modified;
		}

	}

	@Override
	public synchronized void clear() {
		flightCacheItems.clear();
	}

}
