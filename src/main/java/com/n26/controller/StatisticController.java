package com.n26.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.n26.application.StatisticService;
import com.n26.controller.dto.StatisticMapper;
import com.n26.domain.Statistic;

@RestController
@RequestMapping("/statistics")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> get() {
	Statistic statistic = statisticService.get();

	return ResponseEntity.ok(StatisticMapper.makeStatisticDTO(statistic));
    }

}
