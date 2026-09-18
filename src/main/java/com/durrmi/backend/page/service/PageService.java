package com.durrmi.backend.page.service;

import com.durrmi.backend.page.dto.PageResponseDTO;

public interface PageService {

    PageResponseDTO getHomePage();

    PageResponseDTO getPageBySlug(String slug);

    PageResponseDTO getPageById(String id);
}
