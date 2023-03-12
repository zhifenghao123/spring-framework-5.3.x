
package com.hao.mapper;

import java.util.List;

import com.hao.entity.Country;

public interface CountryMapper {
	public void saveCountry(Country country);
	public Country getCountryById(Integer countryId);
	public List<Country> getAllCountrys();
}
