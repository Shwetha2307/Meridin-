package com.example.javprod.service;

import com.example.javprod.dto.AnalyticsSummaryDTO;
import com.example.javprod.dto.TimelineEventDTO;

import java.util.List;

public interface AnalyticsService {
    AnalyticsSummaryDTO getSummary();
    List<TimelineEventDTO> getRecentTimeline();
}
