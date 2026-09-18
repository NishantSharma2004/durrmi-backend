package com.durrmi.backend.page.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionDTO {

    private String id;

    @JsonProperty("order")
    private Integer order;

    private Map<String, Object> component;
}
