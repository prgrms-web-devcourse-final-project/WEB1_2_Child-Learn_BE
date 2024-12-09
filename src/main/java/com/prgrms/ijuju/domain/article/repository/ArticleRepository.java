package com.prgrms.ijuju.domain.article.repository;

import com.prgrms.ijuju.domain.article.contant.DataType;
import com.prgrms.ijuju.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Modifying
    @Transactional
    void deleteByDuration(int duration);

    List<Article> findByStockSymbol(String stockSymbol);

    List<Article> findByType(DataType type);

    long countByType(DataType type);


}