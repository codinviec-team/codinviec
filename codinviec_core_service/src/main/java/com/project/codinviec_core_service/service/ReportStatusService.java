package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.ReportStatusDTO;
import com.project.codinviec_core_service.request.PageRequestCustom;
import com.project.codinviec_core_service.request.ReportStatusRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportStatusService {

    List<ReportStatusDTO> getAllStatus();
    Page<ReportStatusDTO> getAllStatusWithPage(PageRequestCustom req);
    ReportStatusDTO getStatusById(int id);
    ReportStatusDTO createStatus(ReportStatusRequest request);
    ReportStatusDTO updateStatus(int id, ReportStatusRequest request);
    void delete(int id);
}
