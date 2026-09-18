package com.durrmi.backend.page.controller;

import com.durrmi.backend.page.dto.PageMetaDTO;
import com.durrmi.backend.page.dto.PageResponseDTO;
import com.durrmi.backend.page.dto.SectionDTO;
import com.durrmi.backend.page.service.PageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@ExtendWith(MockitoExtension.class)
class PageControllerTest {

    @Mock
    private PageService pageService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private PageResponseDTO sampleResponse;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new PageController(pageService))
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        sampleResponse = PageResponseDTO.builder()
                .page(PageMetaDTO.builder()
                        .id("page_home")
                        .slug("/")
                        .version(1)
                        .build())
                .sections(List.of(
                        SectionDTO.builder()
                                .id("section_hero")
                                .order(2)
                                .component(java.util.Map.of("id", "cmp_hero", "type", "HERO"))
                                .build(),
                        SectionDTO.builder()
                                .id("section_trust")
                                .order(3)
                                .component(java.util.Map.of("id", "cmp_trust", "type", "CONTAINER"))
                                .build()
                ))
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/page/home returns 200 OK and unified page response")
    void getHomePage_Returns200() throws Exception {
        when(pageService.getHomePage()).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/v1/page/home")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.page.id").value("page_home"))
                .andExpect(jsonPath("$.page.slug").value("/"))
                .andExpect(jsonPath("$.page.version").value(1))
                .andExpect(jsonPath("$.sections").isArray())
                .andExpect(jsonPath("$.sections[0].id").value("section_hero"))
                .andExpect(jsonPath("$.sections[0].order").value(2))
                .andExpect(jsonPath("$.sections[0].component.id").value("cmp_hero"))
                .andExpect(jsonPath("$.sections[1].id").value("section_trust"))
                .andExpect(jsonPath("$.sections[1].order").value(3));
    }

    @Test
    @DisplayName("GET /api/v1/page?slug=/ returns 200 OK")
    void getPageBySlug_Returns200() throws Exception {
        when(pageService.getPageBySlug("/")).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/v1/page")
                        .param("slug", "/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.id").value("page_home"));
    }

    @Test
    @DisplayName("GET /api/v1/landing-page returns 200 OK")
    void getLandingPage_Returns200() throws Exception {
        when(pageService.getHomePage()).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/v1/landing-page")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.id").value("page_home"));
    }

    @Test
    @DisplayName("GET /api/v1/page/home returns 404 when home page does not exist")
    void getHomePage_NotFound() throws Exception {
        when(pageService.getHomePage()).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Home page not found"));

        mockMvc.perform(get("/api/v1/page/home")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
