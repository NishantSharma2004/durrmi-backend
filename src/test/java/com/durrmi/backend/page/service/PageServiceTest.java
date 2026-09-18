package com.durrmi.backend.page.service;

import com.durrmi.backend.page.dto.PageResponseDTO;
import com.durrmi.backend.page.entity.PageEntity;
import com.durrmi.backend.page.entity.SectionEntity;
import com.durrmi.backend.page.repository.PageRepository;
import com.durrmi.backend.page.repository.SectionRepository;
import com.durrmi.backend.page.service.impl.PageServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PageServiceTest {

    @Mock
    private PageRepository pageRepository;

    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private PageServiceImpl pageService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private PageEntity samplePage;
    private SectionEntity sampleSection1;
    private SectionEntity sampleSection2;

    @BeforeEach
    void setUp() throws Exception {
        samplePage = PageEntity.builder()
                .id("page_home")
                .slug("/")
                .version(1)
                .build();

        sampleSection1 = SectionEntity.builder()
                .id("section_hero")
                .page(samplePage)
                .displayOrder(2)
                .component(java.util.Map.of("id", "cmp_hero", "type", "HERO"))
                .build();

        sampleSection2 = SectionEntity.builder()
                .id("section_trust")
                .page(samplePage)
                .displayOrder(3)
                .component(java.util.Map.of("id", "cmp_trust", "type", "CONTAINER"))
                .build();
    }

    @Test
    @DisplayName("getHomePage returns complete page with ordered sections")
    void getHomePage_Success() {
        when(pageRepository.findById("page_home")).thenReturn(Optional.of(samplePage));
        when(sectionRepository.findByPageIdOrderByDisplayOrderAsc("page_home"))
                .thenReturn(List.of(sampleSection1, sampleSection2));

        PageResponseDTO response = pageService.getHomePage();

        assertThat(response).isNotNull();
        assertThat(response.getPage()).isNotNull();
        assertThat(response.getPage().getId()).isEqualTo("page_home");
        assertThat(response.getPage().getSlug()).isEqualTo("/");
        assertThat(response.getPage().getVersion()).isEqualTo(1);
        assertThat(response.getSections()).hasSize(2);
        assertThat(response.getSections().get(0).getId()).isEqualTo("section_hero");
        assertThat(response.getSections().get(0).getOrder()).isEqualTo(2);
        assertThat(response.getSections().get(1).getId()).isEqualTo("section_trust");
        assertThat(response.getSections().get(1).getOrder()).isEqualTo(3);

        verify(pageRepository).findById("page_home");
        verify(sectionRepository).findByPageIdOrderByDisplayOrderAsc("page_home");
    }

    @Test
    @DisplayName("getHomePage throws 404 when page does not exist")
    void getHomePage_NotFound() {
        when(pageRepository.findById("page_home")).thenReturn(Optional.empty());
        when(pageRepository.findBySlug("/")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pageService.getHomePage())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Home page not found");
    }

    @Test
    @DisplayName("getPageBySlug returns matching page")
    void getPageBySlug_Success() {
        when(pageRepository.findBySlug("/")).thenReturn(Optional.of(samplePage));
        when(sectionRepository.findByPageIdOrderByDisplayOrderAsc("page_home"))
                .thenReturn(List.of(sampleSection1));

        PageResponseDTO response = pageService.getPageBySlug("/");

        assertThat(response).isNotNull();
        assertThat(response.getPage().getSlug()).isEqualTo("/");
        assertThat(response.getSections()).hasSize(1);
    }
}
