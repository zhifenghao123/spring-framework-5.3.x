
package com.hao.main;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hao.entity.City;
import com.hao.entity.Country;
import com.hao.mapper.CityMapper;
import com.hao.service.CityService;
import com.hao.service.CountryService;

public class ClassPathXmlApplicationContextMain {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		CityService cityService = (CityService) ac.getBean("cityService");
		List<City> citieList = cityService.getAllCitys();
		System.out.println(citieList);

		//CityMapper cityMapper = (CityMapper) ac.getBean("cityMapper");
		//System.out.println(cityMapper);

		CountryService countryService = (CountryService)ac.getBean("countryService");
		List<Country> countryList = countryService.getAllCountrys();
		System.out.println(countryList);

		Country austrilai = new Country();
		austrilai.setCountryId(4);
		austrilai.setCountryName("Austrilai");
		try {
			countryService.save(austrilai);
		} catch (Exception e) {
			System.out.println("error");
		}

	}
}
