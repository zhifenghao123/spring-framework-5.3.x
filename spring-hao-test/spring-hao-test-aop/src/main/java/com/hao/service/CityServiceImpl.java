
package com.hao.service;

import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.hao.entity.City;
import com.hao.entity.CityRowMapper;


public class CityServiceImpl implements CityService {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void save(City city) {
		jdbcTemplate.update("insert into city(city_id,city_name,country_id) values(?,?,?)",
				new Object[]{city.getCityId(), city.getCityName(), city.getCountryId()},
				new int[]{Types.INTEGER, Types.VARCHAR, Types.INTEGER});
	}

	@Override
	public List<City> getAllCitys() {
		@SuppressWarnings({"unchecked"})
		List<City> cityList = jdbcTemplate.query("select * from city", new CityRowMapper());
		return cityList;
	}

	@Override
	public List<City> getCityByCountryId(String countryId) {
		@SuppressWarnings({"unchecked"})
		List<City> cityList = jdbcTemplate.query("select * from city where country_id = ?", new Object[]{},
				new int[]{Types.VARCHAR},
				new CityRowMapper());
		return cityList;
	}

	@Override
	public City getCityByCityId(String cityId) {
		@SuppressWarnings({"unchecked"})
		City city= jdbcTemplate.queryForObject("select * from city where city_id = ?",  City.class);
		return city;
	}
}
