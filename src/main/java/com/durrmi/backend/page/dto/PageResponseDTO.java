package com.durrmi.backend.page.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponseDTO {

    private PageMetaDTO page;
    private List<SectionDTO> sections;
}
