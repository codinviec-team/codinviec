package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.BlogDTO;
import com.project.codinviec_core_service.dto.BlogDetailDTO;
import com.project.codinviec_core_service.request.PageRequestCustom;
import com.project.codinviec_core_service.request.SaveUpdateBlogRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlogService {
     List<BlogDTO> getAllBlog();
     Page<BlogDTO> getAllBlogPage(PageRequestCustom pageRequestCustom);
     BlogDTO getBlogById(Integer id);
     BlogDTO saveBlog(SaveUpdateBlogRequest saveUpdateBlogRequest );
     BlogDTO updateBlogById(Integer idBlog,SaveUpdateBlogRequest saveUpdateBlogRequest);
     BlogDTO deleteBlogById(Integer id);
     BlogDetailDTO getBlogDetailById(Integer id);
}
