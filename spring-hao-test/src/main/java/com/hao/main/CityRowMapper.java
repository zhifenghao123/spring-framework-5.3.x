
package com.hao.main;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hao.entity.City;

public class CityRowMapper implements RowMapper<City> {
	@Override
	public City mapRow(ResultSet rs, int rowNum) throws SQLException {
		City city = new City();
		city.setCityId(rs.getInt("city_id"));
		city.setCityName(rs.getString("city_name"));
		city.setCountryId(rs.getInt("country_id"));
		return city;
	}
}
