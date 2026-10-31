package com.example.javprod.web;

import com.example.javprod.dto.AnalyticsSummaryDTO;
import com.example.javprod.dto.TimelineEventDTO;
import com.example.javprod.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/summary")
    public ResponseEntity<AnalyticsSummaryDTO> getSummary() {
        return ResponseEntity.ok(analyticsService.getSummary());
    }

    @GetMapping("/timeline")
    public ResponseEntity<List<TimelineEventDTO>> getTimeline() {
        return ResponseEntity.ok(analyticsService.getRecentTimeline());
    }
}
