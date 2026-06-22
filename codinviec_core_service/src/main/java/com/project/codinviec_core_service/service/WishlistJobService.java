package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.WishlistJobDTO;
import com.project.codinviec_core_service.request.PageRequestCustom;
import com.project.codinviec_core_service.request.WishlistJobRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WishlistJobService {
    List<WishlistJobDTO> getAllWishlistJobs();
    Page<WishlistJobDTO> getAllWishlistJobsPage(PageRequestCustom pageRequestCustom);
    List<WishlistJobDTO> getWishlistJobsByUserId(String userId);
    List<WishlistJobDTO> saveWishlistJobs(WishlistJobRequest wishlistJobRequest);
    List<WishlistJobDTO> deleteWishlistJobs(WishlistJobRequest wishlistJobRequest);
}
