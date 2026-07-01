package com.warehouse.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.warehouse.entity.StockTransfer;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {

    List<StockTransfer> findBySourceWarehouseId(Long warehouseId);

    List<StockTransfer> findByTargetWarehouseId(Long warehouseId);

    List<StockTransfer> findByProductId(Long productId);
   

}