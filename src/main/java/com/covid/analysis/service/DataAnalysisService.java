package com.covid.analysis.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.covid.analysis.entity.CovidData;
import com.covid.analysis.exception.InvalidStateCodeException;
import com.covid.analysis.exception.NoDataFoundException;

public interface DataAnalysisService {

	public List<String> getStates() throws NoDataFoundException;
	public List<String> getDistrictbyStates(String state) throws NoDataFoundException, InvalidStateCodeException;
	public void loadData() throws NoDataFoundException;
	public Map<String, List<CovidData>> getConfirmedCasesBetweenDates(LocalDate start, LocalDate end) throws NoDataFoundException;
	public Map<LocalDate, List<CovidData>> getConfirmedCasesBetweenDatesGrouped(LocalDate startDate, LocalDate endDate) throws NoDataFoundException;
}
