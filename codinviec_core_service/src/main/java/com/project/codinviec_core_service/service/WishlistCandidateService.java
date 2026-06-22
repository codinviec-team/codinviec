package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.dto.WishlistCandidateDTO;
import com.project.codinviec_core_service.request.PageRequestCustom;
import com.project.codinviec_core_service.request.WishlistCandidateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WishlistCandidateService {
     List<WishlistCandidateDTO> getAllWishlistCandidates();
     Page<WishlistCandidateDTO> getAllWishlistCandidatesPage(PageRequestCustom pageRequestCustom);
    List<WishlistCandidateDTO> getWishlistCandidateByHrId(String hrId);
    List<WishlistCandidateDTO> saveWishlistCandidate(WishlistCandidateRequest saveUpdateWishlistCandidate);
    List<WishlistCandidateDTO> deleteWistListCandidate(WishlistCandidateRequest saveUpdateWishlistCandidate);
}
