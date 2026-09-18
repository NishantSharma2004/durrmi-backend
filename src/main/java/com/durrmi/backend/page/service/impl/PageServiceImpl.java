package com.durrmi.backend.page.service.impl;

import com.durrmi.backend.page.dto.PageMetaDTO;
import com.durrmi.backend.page.dto.PageResponseDTO;
import com.durrmi.backend.page.dto.SectionDTO;
import com.durrmi.backend.page.entity.PageEntity;
import com.durrmi.backend.page.entity.SectionEntity;
import com.durrmi.backend.page.repository.PageRepository;
import com.durrmi.backend.page.repository.SectionRepository;
import com.durrmi.backend.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final SectionRepository sectionRepository;

    @Override
    public PageResponseDTO getHomePage() {
        return pageRepository.findById("page_home")
                .or(() -> pageRepository.findBySlug("/"))
                .map(this::mapToPageResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Home page not found"));
    }

    @Override
    public PageResponseDTO getPageBySlug(String slug) {
        return pageRepository.findBySlug(slug)
                .map(this::mapToPageResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found for slug: " + slug));
    }

    @Override
    public PageResponseDTO getPageById(String id) {
        return pageRepository.findById(id)
                .map(this::mapToPageResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found for id: " + id));
    }

    private PageResponseDTO mapToPageResponseDTO(PageEntity page) {
        List<SectionEntity> sections = sectionRepository.findByPageIdOrderByDisplayOrderAsc(page.getId());

        List<SectionDTO> sectionDTOs = sections.stream()
                .map(sec -> SectionDTO.builder()
                        .id(sec.getId())
                        .order(sec.getDisplayOrder())
                        .component(sec.getComponent())
                        .build())
                .toList();

        return PageResponseDTO.builder()
                .page(PageMetaDTO.builder()
                        .id(page.getId())
                        .slug(page.getSlug())
                        .version(page.getVersion())
                        .build())
                .sections(sectionDTOs)
                .build();
    }
}
