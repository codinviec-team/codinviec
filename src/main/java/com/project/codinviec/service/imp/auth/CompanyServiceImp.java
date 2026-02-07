package com.project.codinviec.service.imp.auth;

import com.project.codinviec.dto.CompanyAddressDTO;
import com.project.codinviec.dto.StatusSpecialDTO;
import com.project.codinviec.dto.auth.CompanyDTO;
import com.project.codinviec.entity.CompanyAddress;
import com.project.codinviec.entity.CompanySize;
import com.project.codinviec.entity.StatusSpecial;
import com.project.codinviec.entity.StatusSpecialCompany;
import com.project.codinviec.entity.auth.Company;
import com.project.codinviec.exception.common.NotFoundIdExceptionHandler;
import com.project.codinviec.mapper.CompanyAddressMapper;
import com.project.codinviec.mapper.CompanySizeMapper;
import com.project.codinviec.mapper.StatusSpecialMapper;
import com.project.codinviec.mapper.auth.CompanyMapper;
import com.project.codinviec.repository.CompanyAddressRepository;
import com.project.codinviec.repository.CompanySizeRepository;
import com.project.codinviec.repository.StatusSpecialCompanyRepository;
import com.project.codinviec.repository.auth.CompanyRepository;
import com.project.codinviec.request.PageRequestCompany;
import com.project.codinviec.request.auth.SaveUpdateCompanyRequest;
import com.project.codinviec.service.auth.CompanyService;
import com.project.codinviec.specification.auth.CompanySpecification;
import com.project.codinviec.util.helper.PageCustomHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImp implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final PageCustomHelper PageCustomHelper;
    private final CompanySpecification companySpecification;
    private final CompanySizeRepository companySizeRepository;
    private final CompanySizeMapper companySizeMapper;
    private final CompanyAddressRepository companyAddressRepository;
    private final CompanyAddressMapper companyAddressMapper;

    private final StatusSpecialCompanyRepository statusSpecialCompanyRepository;
    private final StatusSpecialMapper statusSpecialMapper;

    @Override
    public List<CompanyDTO> getAllCompany() {
        List<Company> listCompany = companyRepository.findAllWithCompanySize();
        List<String> listIdCompany = listCompany.stream().map(Company::getId).toList();

        List<CompanyAddress> companyAddressList = companyAddressRepository.findByCompanyIdsWithLocation(listIdCompany);
        List<StatusSpecialCompany> statusSpecialCompanyRepositoryList = statusSpecialCompanyRepository.findByCompanyIdsWithStatus(listIdCompany);


        Map<String, List<CompanyAddress>> mapCompanyAddress = companyAddressList.stream()
                .collect(Collectors.groupingBy((a -> a.getCompany().getId())));

        Map<String, List<StatusSpecialCompany>> mapStatusSpecical = statusSpecialCompanyRepositoryList.stream()
                .collect(Collectors.groupingBy( a-> a.getIdCompany().getId()));

        List<CompanyDTO> listCompanyDTO = listCompany.stream().map(companyMapper::companyToCompanyDTO).toList();
        for (CompanyDTO companyDTO : listCompanyDTO) {
            companyDTO.setCompanyAddress(
                    mapCompanyAddress.getOrDefault(companyDTO.getId(), List.of()).stream().map(a -> companyAddressMapper.toCompanyAddressDTO(a)).toList()
            );
            companyDTO.setStatusSpecials(
                   statusSpecialMapper.StatusSpecialCompanyToStatusSpecialDTO(mapStatusSpecical.getOrDefault(companyDTO.getId(), List.of()))
            );
        }

        return listCompanyDTO;
    }

    @Override
    public Page<CompanyDTO> getAllCompanyPage(PageRequestCompany pageRequestCompany) {
        // Validate pageCustom
        PageRequestCompany pageRequestValidate = PageCustomHelper.validatePageCompany(pageRequestCompany);
        // Tạo page cho api
        Pageable pageable = PageRequest.of(pageRequestValidate.getPageNumber() - 1,
                pageRequestValidate.getPageSize());

        // Tạo search
        Specification<Company> spec = Specification.allOf(companySpecification.searchByName(pageRequestValidate.getKeyword())
                , companySpecification.minEmployees(pageRequestValidate.getMinEmployees()),
                companySpecification.maxEmployees(pageRequestValidate.getMaxEmployees()),
                companySpecification.hasProvince(pageRequestValidate.getLocation()));

        Page<Company> companyPage = companyRepository.findAll(spec, pageable);
        Page<CompanyDTO> companyDTOPage =  companyPage.map(companyMapper::companyToCompanyDTOForAPIPage);

        List<String> listIdCompany = companyPage.stream().map(Company::getId).toList();
        List<Company> companyForCompanySizes = companyRepository.findAllWithCompanySizeByCompanyIds(listIdCompany);

        List<CompanyAddress> companyAddressList = companyAddressRepository.findByCompanyIdsWithLocation(listIdCompany);
        List<StatusSpecialCompany> statusSpecialCompanyRepositoryList = statusSpecialCompanyRepository.findByCompanyIdsWithStatus(listIdCompany);

        Map<String, List<CompanyAddress>> mapCompanyAddress = companyAddressList.stream()
                .collect(Collectors.groupingBy((a -> a.getCompany().getId())));

        Map<String, List<StatusSpecialCompany>> mapStatusSpecical = statusSpecialCompanyRepositoryList.stream()
                .collect(Collectors.groupingBy( a-> a.getIdCompany().getId()));

        for (CompanyDTO companyDTO : companyDTOPage.getContent()) {
            for (Company company : companyForCompanySizes) {
                if (companyDTO.getId().equals(company.getId())) {
                    companyDTO.setCompanySize(companySizeMapper.companySizeToCompanySizeDTO(company.getCompanySize()));
                }
            }
            companyDTO.setCompanyAddress(
                    mapCompanyAddress.getOrDefault(companyDTO.getId(), List.of()).stream().map(a -> companyAddressMapper.toCompanyAddressDTO(a)).toList()
            );

            companyDTO.setStatusSpecials(
                    statusSpecialMapper.StatusSpecialCompanyToStatusSpecialDTO(mapStatusSpecical.getOrDefault(companyDTO.getId(), List.of()))
            );
        }
        return companyDTOPage;
    }

    @Override
    public CompanyDTO getCompanyById(String idCompany) {
        Company company = companyRepository.findById(idCompany)
                .orElseThrow(() -> new NotFoundIdExceptionHandler("Không tìm thấy id company!"));
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);
        companyDTO.setStatusSpecials(statusSpecialMapper
                .StatusSpecialCompanyToStatusSpecialDTO(statusSpecialCompanyRepository
                        .findByIdCompany_Id(companyDTO.getId())));
        companyDTO.setCompanySize(companySizeMapper.companySizeToCompanySizeDTO(companySizeRepository
                .findByCompanies_Id(companyDTO
                        .getId()).orElse(null)));

        companyDTO.setCompanyAddress(
                companyAddressRepository.findByCompany_Id(companyDTO.getId())
                        .stream().map(companyAddressMapper::toCompanyAddressDTO).toList()
        );
        return companyDTO;
    }

    @Override
    @Transactional
    public CompanyDTO saveCompany(SaveUpdateCompanyRequest saveUpdateCompanyRequest) {
        CompanySize companySize = companySizeRepository.findById(saveUpdateCompanyRequest.getCompanySizeId())
                .orElseThrow(() -> new NotFoundIdExceptionHandler("Không tìm thấy id company size!"));
        Company company = companyMapper.saveCompanyMapper(companySize, saveUpdateCompanyRequest);
        return companyMapper.companyToCompanyDTO(companyRepository.save(company));
    }

    @Override
    @Transactional
    public CompanyDTO updateCompany(String idCompany, SaveUpdateCompanyRequest saveUpdateCompanyRequest) {
        CompanySize companySize = companySizeRepository.findById(saveUpdateCompanyRequest.getCompanySizeId())
                .orElseThrow(() -> new NotFoundIdExceptionHandler("Không tìm thấy id company size!"));

        Company company = companyRepository.findById(idCompany)
                .orElseThrow(() -> new NotFoundIdExceptionHandler("Không tìm thấy id company!"));

        Company mappedCompany = companyMapper.updateCompanyMapper(idCompany, companySize,
                saveUpdateCompanyRequest);
        mappedCompany.setCreatedDate(company.getCreatedDate());
        return companyMapper.companyToCompanyDTO(companyRepository.save(mappedCompany));
    }

    @Override
    @Transactional
    public CompanyDTO deleteCompany(String idCompany) {
        Company company = companyRepository.findById(idCompany)
                .orElseThrow(() -> new NotFoundIdExceptionHandler("Không tìm thấy id company!"));
        companyRepository.delete(company);
        return companyMapper.companyToCompanyDTO(company);
    }
}
