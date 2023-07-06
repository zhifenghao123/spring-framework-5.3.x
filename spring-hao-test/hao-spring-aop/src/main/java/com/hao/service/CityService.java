
package com.hao.service;

import java.util.List;

import com.hao.entity.City;

public interface CityService {
	public void save(City city);
	public List<City> getAllCitys();
	public List<City> getCityByCountryId(String countryId);
	public City getCityByCityId(String cityId);
}
