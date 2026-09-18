package com.durrmi.backend.page.controller;

import com.durrmi.backend.page.dto.PageResponseDTO;
import com.durrmi.backend.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PageController {

    private final PageService pageService;

    /**
     * Unified GET API returning all landing page data in a single request when the website opens.
     */
    @GetMapping("/page/home")
    public ResponseEntity<PageResponseDTO> getHomePage() {
        return ResponseEntity.ok(pageService.getHomePage());
    }

    /**
     * Flexible lookup by slug, e.g. GET /api/v1/page?slug=/
     */
    @GetMapping("/page")
    public ResponseEntity<PageResponseDTO> getPageBySlug(@RequestParam(name = "slug", defaultValue = "/") String slug) {
        return ResponseEntity.ok(pageService.getPageBySlug(slug));
    }

    /**
     * Alias endpoint for landing page
     */
    @GetMapping("/landing-page")
    public ResponseEntity<PageResponseDTO> getLandingPage() {
        return ResponseEntity.ok(pageService.getHomePage());
    }
}
