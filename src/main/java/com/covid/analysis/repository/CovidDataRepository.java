package com.covid.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.covid.analysis.entity.CovidData;

@Repository
public interface CovidDataRepository extends JpaRepository<CovidData, Long>{

}
