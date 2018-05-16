package com.n26.controller.dto;

import com.n26.domain.Statistic;

public class StatisticMapper {

    public static StatisticDTO makeStatisticDTO(final Statistic statistic) {
	StatisticDTO statisticDTO = new StatisticDTO();
	statisticDTO.setSum(statistic.getSum().getNumber().doubleValue());
	statisticDTO.setAvg(statistic.getAvg().getNumber().doubleValue());
	statisticDTO.setMax(statistic.getMax().getNumber().doubleValue());
	statisticDTO.setMin(statistic.getMin().getNumber().doubleValue());
	statisticDTO.setCount(statistic.getCount());

	return statisticDTO;
    }

}
