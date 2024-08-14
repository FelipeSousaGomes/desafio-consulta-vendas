package com.devsuperior.dsmeta.repositories;

import java.util.List;
import com.devsuperior.dsmeta.dto.ReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(value = "SELECT new com.devsuperior.dsmeta.dto.ReportDTO(obj.id, obj.amount, obj.date, obj.seller.name)" +
            " FROM Sale obj" +
            " WHERE obj.date BETWEEN :dataMinima AND :dataMaxima" +
            " AND UPPER(obj.seller.name) LIKE CONCAT('%', UPPER(:name), '%')")
    Page<List<ReportDTO>> searchReport(LocalDate dataMinima, LocalDate dataMaxima, String name, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT tb_seller.name AS sellerName, SUM(tb_sales.amount) AS total " +
            "FROM tb_sales " +
            "INNER JOIN tb_seller " +
            "ON tb_sales.seller_id = tb_seller.id " +
            "WHERE tb_sales.date BETWEEN :minima AND :maxima " +
            "GROUP BY tb_seller.name")
    List<SumaryProjection> searchSummary(LocalDate minima, LocalDate maxima);

}
