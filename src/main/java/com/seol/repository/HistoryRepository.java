package com.seol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seol.entity.History;

public interface HistoryRepository extends JpaRepository<History, String> {
    public List<History> findTop10ByIdOrderByRegdateDesc(String id);
}
