package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.JobLevelDTO;
import com.project.codinviec_core_service.request.JobLevelRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobLevelService {
    List<JobLevelDTO> getAll();
    Page<JobLevelDTO> getAllWithPage(PageRequestCustom req);
    JobLevelDTO getById(int id);
    JobLevelDTO create(JobLevelRequest request);
    JobLevelDTO update(int id, JobLevelRequest request);
    void delete(int id);

}
