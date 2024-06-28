package com.example.btpn.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.btpn.dto.Items.CreateItemReq;
import com.example.btpn.dto.Items.GetAllItemRes;
import com.example.btpn.dto.Items.GetItemByIdRes;
import com.example.btpn.dto.Items.UpdateItemReq;
import com.example.btpn.models.Items;
import com.example.btpn.repositories.ItemsRepo;
import com.example.btpn.services.specifications.ItemSpec;

@Service
public class ItemsService {
    @Autowired
    ItemsRepo itemsRepo;

    @Autowired
    ItemSpec itemSpec;

    public List<GetAllItemRes> getAllItems(Pageable pageable, String filterBy, String filterValue, String sortBy){
        List<Items> items = new ArrayList<>();

        if(filterValue.equals("")){
            items = itemsRepo.findAll();
        }
        else{ // Filtering here
            Specification<Items> filterOption = itemSpec.filterSpec(filterBy, filterValue);
            items = itemsRepo.findAll(filterOption);
        }
        Integer totalItems = items.size();

        // Sorting
        Comparator<Items> sortComparator = itemSpec.sortComparatorSpec(sortBy);
        Collections.sort(items, sortComparator);

        // Converting Items from List to Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), items.size());
        List<Items> sublist = items.subList(start, end);
        Page<Items> itemPage = new PageImpl<>(sublist, pageable, items.size());

        List<GetAllItemRes> getAllItemRes = itemPage.stream().map(item -> {
            GetAllItemRes res = new GetAllItemRes();
            res.setTotalRecord(totalItems);
            res.setItemsId(item.getItemId());
            res.setItemsName(item.getItemName());
            res.setItemsCode(item.getItemCode());
            res.setStock(item.getStock());
            res.setPrice(item.getPrice());
            res.setIsAvailable(item.getIsAvailable());
            res.setLastReStock(item.getLastReStock());
            return res;
        })
        .collect(Collectors.toList());
        return getAllItemRes;
    }

    public GetItemByIdRes getItemsById(Integer id){
        Optional<Items> items = itemsRepo.findById(id);
        GetItemByIdRes getItemByIdRes = new GetItemByIdRes();
        getItemByIdRes.setItemId(items.get().getItemId());
        getItemByIdRes.setItemName(items.get().getItemName());
        getItemByIdRes.setItemCode(items.get().getItemCode());
        getItemByIdRes.setStock(items.get().getStock());
        getItemByIdRes.setPrice(items.get().getPrice());
        getItemByIdRes.setIsAvailable(items.get().getIsAvailable());
        getItemByIdRes.setLastReStock(items.get().getLastReStock());

        return getItemByIdRes;
    }

    public void createItem(CreateItemReq createItemReq){
        Items items = new Items();
        items.setItemName(createItemReq.getItemName());
        items.setItemCode("");
        items.setStock(createItemReq.getStock());
        items.setPrice(createItemReq.getPrice());
        items.setIsAvailable(1);
        if(createItemReq.getStock() > 0){
            items.setLastReStock(LocalDateTime.now());
        }
        itemsRepo.save(items);

        items.setItemCode("ITEM-" + items.getItemId());
        itemsRepo.save(items);
    }

    public void updateItems(Integer id, UpdateItemReq updateItemReq){
        Optional<Items> optionalItems = itemsRepo.findById(id);
        Items items = optionalItems.get();
        items.setStock(updateItemReq.getStock());
        items.setPrice(updateItemReq.getPrice());
        itemsRepo.save(items);
    }

    public Items deleteItems(Integer id){
        Optional<Items> optionalItems = itemsRepo.findById(id);
        optionalItems.get().setIsAvailable(0);
        return optionalItems.get();
    }

    public List<String> getItemsName(){
        List<Items> items = itemsRepo.findAll();
        List<String> itemsName = items.stream().map(item -> {
            String name = item.getItemName();
            return name;
        })
        .collect(Collectors.toList());
        return itemsName;
    }
}