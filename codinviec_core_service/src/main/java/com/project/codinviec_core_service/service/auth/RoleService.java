package com.project.codinviec_core_service.service.auth;

import com.project.codinviec_core_service.dto.auth.RoleDTO;
import com.project.codinviec_core_service.request.PageRequestCustom;
import com.project.codinviec_core_service.request.auth.RoleRequest;
import org.springframework.data.domain.Page;

import java.util.List;


public interface RoleService {
    List<RoleDTO> getAll();
    Page<RoleDTO> getAllWithPage(PageRequestCustom req);
    RoleDTO getById(String id);
    RoleDTO create(RoleRequest req);
    RoleDTO update(String id, RoleRequest req);
    RoleDTO deleteById(String id);
}
