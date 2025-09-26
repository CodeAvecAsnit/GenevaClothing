package com.ecomm.np.genevaecommerce.service.modelservice.impl;

import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.GenderTable;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.repository.ItemsRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.ItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemsRepository itemsRepository;

    @Autowired
    public ItemServiceImpl(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    @Transactional
    public Items findItemById(int id){
        return itemsRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("The requested item was not found."));
    }

    @Override
    @Transactional
    public void saveItem(Items item){
        itemsRepository.save(item);
    }

    @Override
    @Transactional
    public Page<Items> findGenderItems(GenderTable genderTable, Pageable pageable) {
        return itemsRepository.findByGenderTable(genderTable, pageable);
    }

    @Override
    @Transactional
    public Page<Items> findAllTheItems(Pageable page){
        return itemsRepository.findAll(page);
    }

    @Override
    @Transactional
    public List<Items> findTop10(){
        return itemsRepository.findTop10ByOrderByCreatedDateDesc();
    }

    @Override
    @Transactional
    public Long findTotalItemCount() {
        return itemsRepository.findTotalItems();
    }

    @Override
    @Transactional
    public List<Items> findAllByListOfIds(List<Integer> ids) {
        return itemsRepository.findAllById(ids);
    }

    @Override
    @Transactional
    public List<Items> getRandomItems(){
        return itemsRepository.findRandomItems();
    }

    @Override
    public List<Items> findAllById(List<Integer> ids) {
        return itemsRepository.findAllById(ids);
    }
}

