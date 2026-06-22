package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.ReportDTO;
import com.project.codinviec_core_service.request.PageRequestCustom;
import com.project.codinviec_core_service.request.ReportRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportService {
    List<ReportDTO> getAllReports();

    ReportDTO getReportById(int id);
    ReportDTO createReport(ReportRequest request);
    ReportDTO updateReport(int id, ReportRequest request);
    void deleteReport(int id);
    Page<ReportDTO> getAllReportsPage(PageRequestCustom request);
}
