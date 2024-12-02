package com.prgrms.ijuju.domain.article.repository;

import com.prgrms.ijuju.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 특정 주식 심볼로 기사 조회
    List<Article> findByStockSymbol(String stockSymbol);

    // 유지 기간이 지난 기사 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM articles WHERE created_at + INTERVAL duration DAY <= :now", nativeQuery = true)
    void deleteByExpiration(LocalDateTime now);
}