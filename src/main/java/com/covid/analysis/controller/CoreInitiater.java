package com.covid.analysis.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.covid.analysis.entity.CovidData;
import com.covid.analysis.exception.InvalidDateException;
import com.covid.analysis.exception.InvalidDateRangeException;
import com.covid.analysis.exception.InvalidStateCodeException;
import com.covid.analysis.exception.InvalidValidationParameterException;
import com.covid.analysis.exception.NoDataFoundException;
import com.covid.analysis.validation.InputValidation;

@Component
public class CoreInitiater {

	@Autowired
	MenuController menu;

	@Autowired
	InputValidation validationDriver;

	Scanner sc = new Scanner(System.in);

	public void initiate() {

		try {
			callMenu();
		} catch (NoDataFoundException | InvalidValidationParameterException | InvalidStateCodeException
				| InvalidDateException | InvalidDateRangeException e) {
			//e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			initiate();
		}
	}

	private void callMenu() throws NoDataFoundException, InvalidValidationParameterException, InvalidStateCodeException,
			InvalidDateException, InvalidDateRangeException {
		System.out.println(">>Select options from the menu below...");
		int option = getOption();
		if (option == 5) {
			// do Nothing, return control to starter class
		} else if (option == 1) {
			List<String> states = menu.getStates();
			states.forEach(System.out::println);
			callMenu();

		} else if (option == 2) {
			System.out.print("Please enter state code : ");
			String state = sc.next();

			if (!validationDriver.validationDriver(state, "State")) {
				throw new InvalidStateCodeException("Invalid State Code, please check your input");
			}

			List<String> district = menu.getDistrictbyState(state);
			if (district != null) {
				district.forEach(System.out::println);
			}
			callMenu();

		} else if (option == 3) {
			LocalDate startDate;
			LocalDate endDate;

			System.out.println("Please enter start date (YYYY-MM-DD) : ");
			startDate = inputDate("Start");

			System.out.println("Please enter end date (YYYY-MM-DD) : ");
			endDate = inputDate("End");

			if (startDate != null && endDate != null) {
				if (endDate.isBefore(startDate)) {
					throw new InvalidDateRangeException("Invalid Date Range, please check your input");
				}
				Map<String, List<CovidData>> dataByState = menu.getConfimedCasesBetweenDates(startDate, endDate);
				printDataByStateDate(dataByState);
			}
			callMenu();

		} else if (option == 4) {
			LocalDate startDate;
			LocalDate endDate;
			
			System.out.println("Please enter start date (YYYY-MM-DD) : ");
			startDate = inputDate("Start");
			System.out.println("Please enter end date (YYYY-MM-DD) : ");
			endDate = inputDate("End");
			
			System.out.println("Please enter first state code : ");
			String firstState = sc.next();
			System.out.println("Please enter second state code : ");
			String secondState = sc.next();
			
			if (startDate != null && endDate != null) {
				if (endDate.isBefore(startDate)) {
					throw new InvalidDateRangeException("Invalid Date Range, please check your input");
				}
				if (!validationDriver.validationDriver(firstState, "State")) {
					throw new InvalidStateCodeException("Invalid State Code, please check your input");
				}
				if (!validationDriver.validationDriver(secondState, "State")) {
					throw new InvalidStateCodeException("Invalid State Code, please check your input");
				}
				Map<LocalDate, List<CovidData>> dataByDates = menu.getConfimedCasesBetweenStateDates(startDate, endDate);
				printDataByStateDate(dataByDates, firstState, secondState);
			}
			callMenu();

		} else {
			System.out.println("Wrong input, please select from below menu");
			callMenu();
		}
	}

	private int getOption() {
		System.out.println("1.Get States Name.\r\n" + "2.Get District name for given states\r\n"
				+ "3.Display Data by state with in date range\r\n"
				+ "4.Display Confirmed cases by comparing two states for a given date range.\r\n" + "5.Exit");
		System.out.print(">>Please select Option : ");
		int option = sc.nextInt();
		return option;
	}

	private void printDataByStateDate(Map<String, List<CovidData>> dataMap) {

		Set<String> states = dataMap.keySet();

		System.out.println("       Date|  State  |  Confirmed Total");
		for (String state : states) {
			List<LocalDate> dates = (dataMap.get(state)).stream().map(c -> c.getDate()).distinct()
					.collect(Collectors.toList());
			Map<LocalDate, List<CovidData>> mapByDate = (dataMap.get(state)).stream()
					.collect(Collectors.groupingBy(c -> c.getDate()));
			for (LocalDate date : dates) {
				System.out.println(date + " |   " + state + "    |      " + (mapByDate.get(date)).size());
			}

		}
	}

	private void printDataByStateDate(Map<LocalDate, List<CovidData>> dataByDates, String firstState,
			String secondState) {
		Set<LocalDate> dates = dataByDates.keySet();
		Predicate<CovidData> predicateForFirstState = c -> c.getState().equals(firstState);
		Predicate<CovidData> predicateForSecondState = c -> c.getState().equals(secondState);
		System.out.println(
				"DATE       |    FIRST STATE    |    FIRST STATE CONFIRMED TOTAL    |    SECOND STATE    |    SECOND STATE CONFIRMED TOTAL");
		for (LocalDate date : dates) {
			int totalCaseFS = (dataByDates.get(date).stream().filter(predicateForFirstState)
					.collect(Collectors.toList())).size();
			int totalCaseSS = (dataByDates.get(date).stream().filter(predicateForSecondState)
					.collect(Collectors.toList())).size();
			System.out.println(date + " |         " + firstState + "        |                " + totalCaseFS
					+ "                 |         " + secondState + "        |                " + totalCaseSS);
		}

	}

	private LocalDate inputDate(String dateType) throws InvalidDateException {

		LocalDate date = null;

		try {
			date = LocalDate.parse(sc.next());
		} catch (Exception e) {
			if (dateType.equals("Start")) {
				throw new InvalidDateException("Invalid start Date, please check your input");
			} else if (dateType.equals("End")) {
				throw new InvalidDateException("Invalid end Date, please check your input");
			}
		}

		return date;
	}

}
