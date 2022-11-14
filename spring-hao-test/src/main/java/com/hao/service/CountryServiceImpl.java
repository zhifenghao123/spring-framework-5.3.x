
package com.hao.service;

import java.util.List;

import com.hao.entity.Country;
import com.hao.mapper.CountryMapper;

public class CountryServiceImpl implements CountryService {
	private CountryMapper countryMapper;

	public void setCountryMapper(CountryMapper countryMapper) {
		this.countryMapper = countryMapper;
	}

	@Override
	public void save(Country country) {
		countryMapper.saveCountry(country);
		throw new RuntimeException();
	}

	@Override
	public Country getCountryById(Integer countryId) {
		return null;
	}

	@Override
	public List<Country> getAllCountrys() {
		return countryMapper.getAllCountrys();
	}
}
