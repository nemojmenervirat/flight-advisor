package com.github.nemojmenervirat.flightadvisor.service;

import com.github.nemojmenervirat.flightadvisor.model.Route;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseProcessor;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseRoutesContext;

public interface RouteService extends ParseProcessor<Route, ParseRoutesContext> {

}
