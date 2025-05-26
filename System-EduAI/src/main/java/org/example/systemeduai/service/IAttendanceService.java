package org.example.systemeduai.service;

import java.util.Map;

public interface IAttendanceService {
    Map<String, Object> recordAttendance(Integer classId, String childName, String checkInTime, String checkOutTime, String date);

    Map<String, Object> getAttendanceByClassId(Integer classId, String date);
}
