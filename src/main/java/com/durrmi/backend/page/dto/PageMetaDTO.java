package com.durrmi.backend.page.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageMetaDTO {
    private String id;
    private String slug;
    private Integer version;
}
