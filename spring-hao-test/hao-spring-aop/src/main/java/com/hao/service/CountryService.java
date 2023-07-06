
package com.hao.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hao.entity.Country;


@Transactional(propagation = Propagation.REQUIRED)
public interface CountryService {
	public void save(Country country);
	public Country getCountryById(Integer countryId);
	public List<Country> getAllCountrys();
}
