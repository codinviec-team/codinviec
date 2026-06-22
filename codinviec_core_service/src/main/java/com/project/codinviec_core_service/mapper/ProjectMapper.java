package com.project.codinviec_core_service.mapper;

import com.project.codinviec_core_service.dto.ProjectDTO;
import com.project.codinviec_core_service.entity.Project;
import com.project.codinviec_core_service.entity.auth.User;
import com.project.codinviec_core_service.request.ProjectRequest;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    public ProjectDTO toDto(Project project) {
        if (project == null) return null;
        return ProjectDTO.builder()
                .id(project.getId())
                .userId(project.getUser().getId())
                .name(project.getName())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .company(project.getCompany())
                .build();
    }

    public Project saveProject(User user, ProjectRequest request) {
        if (request == null) return null;
        return Project.builder()
                .user(user)
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .projectUrl(request.getProjectUrl())
                .company(request.getCompany())
                .build();
    }

    public Project updateProject(Integer id, User user, ProjectRequest request) {
        if (request == null) return null;
        return Project.builder()
                .id(id)
                .user(user)
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .projectUrl(request.getProjectUrl())
                .company(request.getCompany())
                .build();
    }
}
