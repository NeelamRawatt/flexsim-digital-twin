package com.example.flexsim_simulation_service.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController

@RequestMapping("/api/debug")

@AllArgsConstructor

public class DebugController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/db-check")

    public Map<String, Object> checkDb() {

        Map<String, Object> result = new LinkedHashMap<>();

        String currentDb = jdbcTemplate.queryForObject("select current_database()", String.class);

        String currentSchema = jdbcTemplate.queryForObject("select current_schema()", String.class);

        Integer totalCount = jdbcTemplate.queryForObject("select count(*) from public.parcel_data", Integer.class);

        Integer dateCount = jdbcTemplate.queryForObject(

                "select count(*) from public.parcel_data where parcel_date = DATE '2024-11-29'",

                Integer.class

        );

        Integer rangeCount = jdbcTemplate.queryForObject(

                "select count(*) from public.parcel_data where parcel_date = DATE '2024-11-29' and parcel_timestamp between '04:30:00' and '06:00:00'",

                Integer.class

        );

        result.put("currentDatabase", currentDb);

        result.put("currentSchema", currentSchema);

        result.put("totalCount", totalCount);

        result.put("dateCount", dateCount);

        result.put("rangeCount", rangeCount);

        return result;

    }

}
 