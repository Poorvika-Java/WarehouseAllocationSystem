package com.warehouse.serviceimpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warehouse.entity.AllocationHistory;
import com.warehouse.exception.AllocationHistoryNotFoundException;
import com.warehouse.mapper.AllocationHistoryMapper;
import com.warehouse.repository.AllocationHistoryRepository;
import com.warehouse.responsedto.AllocationHistoryResponse;
import com.warehouse.service.AllocationHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AllocationHistoryServiceImpl implements AllocationHistoryService {

    private final AllocationHistoryRepository historyRepository;
    private final AllocationHistoryMapper historyMapper;
    
    @Override
    public AllocationHistoryResponse getHistoryById(Long id) {
        log.info("Fetching Allocation History : {}", id);
        AllocationHistory history = getHistory(id);
        return historyMapper.toResponse(history);

    }
    
    @Override
    public Page<AllocationHistoryResponse> getAllHistory(int page,int size,String sortBy,String direction) {
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =PageRequest.of(page, size, sort);
        return historyRepository
                .findAll(pageable)
                .map(historyMapper::toResponse);

    }
    
    @Override
    public Page<AllocationHistoryResponse> getHistoryByProduct(Long productId,int page,int size) {
        Pageable pageable =PageRequest.of(page, size);
        return historyRepository
                .findByAllocationProductId(productId, pageable)
                .map(historyMapper::toResponse);

    }
    
    @Override
    public Page<AllocationHistoryResponse> getHistoryByWarehouse(Long warehouseId,int page,int size) {
        Pageable pageable =PageRequest.of(page, size);
        return historyRepository
                .findByAllocationWarehouseId(
                        warehouseId,
                        pageable)
                .map(historyMapper::toResponse);

    }
    
    private AllocationHistory getHistory(Long id) {
        return historyRepository
                .findById(id)
                .orElseThrow(() ->
                        new AllocationHistoryNotFoundException("Allocation history not found with id : " + id));

    }

}
