package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.JobDTO;
import com.project.codinviec_core_service.request.ApplyJobRequest;
import com.project.codinviec_core_service.request.GetJobFeaturedRequest;
import com.project.codinviec_core_service.request.JobFilterRequest;
import com.project.codinviec_core_service.request.JobRequest;
import com.project.codinviec_core_service.request.PageRequestCustom;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobService {
    List<JobDTO> getAllJob();
    Page<JobDTO> getAllJobPage(PageRequestCustom pageRequestCustom);
    Page<JobDTO> getAllJobPageWithFilter(JobFilterRequest jobFilterRequest);

    JobDTO getJobById(int id);
    List<JobDTO> getJobByIdCompany(String companyId);

    List<JobDTO> getFeaturedJobs(GetJobFeaturedRequest request);

    JobDTO createJob(JobRequest request);
    JobDTO updateJob(int id, JobRequest request);
    void deleteJob(int id);
    JobDTO applyJob(ApplyJobRequest applyJobRequest);
}
