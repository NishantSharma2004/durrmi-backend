package com.durrmi.backend.page.repository;

import com.durrmi.backend.page.entity.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, String> {

    List<SectionEntity> findByPageIdOrderByDisplayOrderAsc(String pageId);
}
