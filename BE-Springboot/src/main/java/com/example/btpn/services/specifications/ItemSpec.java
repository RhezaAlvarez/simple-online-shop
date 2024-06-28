package com.example.btpn.services.specifications;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.btpn.models.Items;

@Component
public class ItemSpec {
    public Specification<Items> filterByName(String itemName){
        return (root, query, builder) -> builder.like(builder.lower(root.get("itemName")), "%" + itemName.toLowerCase() + "%");
    }

    public Specification<Items> filterSpec(String filterBy, String filterValue){
        Map<String, Specification<Items>> filters = new HashMap<>();
        filters.put("itemName", filterByName(filterValue));
        
        return filters.get(filterBy);
    }

    public Comparator<Items> sortComparatorSpec(String sortBy){
        Map<String, Comparator<Items>> sorts = new HashMap<>();
        sorts.put("Name asc", Comparator.comparing(Items::getItemName));
        sorts.put("Name desc", Comparator.comparing(Items::getItemName).reversed());
        sorts.put("Stock asc", Comparator.comparing(Items::getStock));
        sorts.put("Stock desc", Comparator.comparing(Items::getStock).reversed());
        sorts.put("Price asc", Comparator.comparing(Items::getPrice));
        sorts.put("Price desc", Comparator.comparing(Items::getPrice).reversed());
        sorts.put("Last Re Stock asc", Comparator.comparing(Items::getLastReStock));
        sorts.put("Last Re Stock desc", Comparator.comparing(Items::getLastReStock).reversed());

        return sorts.get(sortBy);
    }
}
