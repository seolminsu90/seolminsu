package com.seol.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.seol.entity.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, String> {
    @Transactional
    @Modifying
    @Query(value = "MERGE INTO TB_SEARCH_KEYWORD" 
                + "    USING DUAL" 
                + "    ON (KEYWORD =  ?1)"
                + "    WHEN MATCHED THEN UPDATE SET SEARCH_COUNT = SEARCH_COUNT + 1"
                + "    WHEN NOT MATCHED THEN INSERT (KEYWORD) VALUES (?1)"
                , nativeQuery = true)
    public int upsertCount(String keyword);

    public List<Keyword> findTop10ByOrderBySearchCountDesc();
}
