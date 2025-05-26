package org.example.systemeduai.service;

import org.example.systemeduai.dto.health.HealthInfoDTO;

import java.util.Map;

public interface IHealthRecordService {

    HealthInfoDTO saveHealthRecord(HealthInfoDTO healthInfoDTO);

    HealthInfoDTO getHealthRecord(Integer studentId);
}
