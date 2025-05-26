package org.example.systemeduai.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface IChildrenService {

    void addChild(Integer classId, String name, List<Double> embedding) throws JsonProcessingException;

    Map<String, Object> getChildrenByClassId(Integer classId);
}
