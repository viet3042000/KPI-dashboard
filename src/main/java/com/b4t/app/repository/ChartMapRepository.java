package com.b4t.app.repository;

import com.b4t.app.service.dto.ChartMapParramDTO;
import com.b4t.app.service.dto.DataChartMapDTO;
import com.b4t.app.service.dto.RangeColorDTO;

import java.util.List;

public interface ChartMapRepository {
    List<DataChartMapDTO> getChartMapsData(ChartMapParramDTO chartMapParramDTO);

    Object getMaxTime(ChartMapParramDTO chartMapParramDTO);

    List<RangeColorDTO> getRangeColor(RangeColorDTO rangeColorDTO);

    Long getScreenIdMap(Long profileId);
}
