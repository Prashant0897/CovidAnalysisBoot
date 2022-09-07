package com.covid.analysis.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.covid.analysis.entity.CovidData;
import com.covid.analysis.exception.InvalidStateCodeException;
import com.covid.analysis.exception.NoDataFoundException;
import com.covid.analysis.service.DataAnalysisService;

@RestController
@RequestMapping("/covidAnalysis")
public class MenuController {
	
	@Autowired
	DataAnalysisService service;
	
	@GetMapping(value = "/getStates")
	public List<String> getStates() throws NoDataFoundException {
		return service.getStates();
	}
	
	@GetMapping(value = "/getDistrictbyState/{state}")
	public List<String> getDistrictbyState(@PathVariable String state) throws NoDataFoundException, InvalidStateCodeException{
		List<String> district = service.getDistrictbyStates(state);
		return district;
	}
	
	@GetMapping(value = "/getConfimedCasesBetweenDates", params = "{start,end}")
	public Map<String,List<CovidData>> getConfimedCasesBetweenDates(@PathVariable LocalDate start, @PathVariable LocalDate end) throws NoDataFoundException{
		Map<String, List<CovidData>> dataByState = service.getConfirmedCasesBetweenDates(start,end);
		return dataByState;
	}

	public Map<LocalDate, List<CovidData>> getConfimedCasesBetweenStateDates(LocalDate startDate, LocalDate endDate) throws NoDataFoundException {
		Map<LocalDate, List<CovidData>> dataByDate = service.getConfirmedCasesBetweenDatesGrouped(startDate,endDate);
		return dataByDate;
	}
}
