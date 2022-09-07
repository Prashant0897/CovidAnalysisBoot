package com.covid.analysis.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covid.analysis.entity.CovidData;
import com.covid.analysis.exception.InvalidStateCodeException;
import com.covid.analysis.exception.NoDataFoundException;
import com.covid.analysis.repository.CovidDataRepository;

@Service
public class DataAnalysisServiceImpl implements DataAnalysisService {

	@Autowired
	CovidDataRepository repo;

	List<CovidData> data = new ArrayList<>();

	@Override
	public void loadData() throws NoDataFoundException {
		this.data = repo.findAll();
		if(data == null || data.isEmpty()) {
			throw(new NoDataFoundException("No data present"));
		}
	}
	
	@Override
	public List<String> getStates() throws NoDataFoundException {
		if (data.isEmpty()) {
			loadData();
		}
		List<String> states = data.stream().map(covidData -> covidData.getState()).distinct()
				.collect(Collectors.toList());
		return states;
	}

	@Override
	public List<String> getDistrictbyStates(String state) throws NoDataFoundException, InvalidStateCodeException {
		if (data.isEmpty()) {
			loadData();
		}
		if (!(data.stream().anyMatch(covidData -> covidData.getState().equals(state)))) {
			throw new InvalidStateCodeException("Invalid State code ("+state+"), please check your input");
		}
		Predicate<CovidData> predicate = covidData -> covidData.getState().equals(state);
		List<String> district = (data.stream().filter(predicate).collect(Collectors.toList())).stream()
				.map(covidData -> covidData.getDistrict()).distinct().sorted().collect(Collectors.toList());
		return district;
	}

	@Override
	public Map<String, List<CovidData>> getConfirmedCasesBetweenDates(LocalDate start, LocalDate end) throws NoDataFoundException {
		if (data.isEmpty()) {
			loadData();
		}

		long bwDays = ChronoUnit.DAYS.between(start, end);
		Map<String, List<CovidData>> map = data.stream().limit(bwDays)
				.collect(Collectors.groupingBy(c -> c.getState()));

		return map;
	}

	@Override
	public Map<LocalDate, List<CovidData>> getConfirmedCasesBetweenDatesGrouped(LocalDate startDate,
			LocalDate endDate) throws NoDataFoundException {
		if (data.isEmpty()) {
			loadData();
		}

		long bwDays = ChronoUnit.DAYS.between(startDate, endDate);
		Map<LocalDate, List<CovidData>> map = data.stream().limit(bwDays)
				.collect(Collectors.groupingBy(c -> c.getDate()));

		return map;
	}

}
