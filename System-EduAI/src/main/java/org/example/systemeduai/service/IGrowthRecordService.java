package org.example.systemeduai.service;

import org.example.systemeduai.dto.health.GrowthRecordDTO;

import java.util.List;
import java.util.Map;

public interface IGrowthRecordService {
    GrowthRecordDTO saveGrowthRecord(GrowthRecordDTO growthRecordDTO);

    GrowthRecordDTO updateGrowthRecord(Integer growthRecordId, GrowthRecordDTO growthRecordDTO);

    List<GrowthRecordDTO> getGrowthRecords(Integer studentId);

    List<Map<String, Object>> getGrowthChartData(Integer studentId);
}
