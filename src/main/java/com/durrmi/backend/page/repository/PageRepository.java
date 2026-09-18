package com.durrmi.backend.page.repository;

import com.durrmi.backend.page.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, String> {

    Optional<PageEntity> findBySlug(String slug);
}
