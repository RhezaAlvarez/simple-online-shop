package com.example.btpn.services.specifications;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.btpn.models.Customers;

@Component
public class CustomerSpec {
    public Specification<Customers> filterByName(String customerName){
        return (root, query, builder) -> builder.like(builder.lower(root.get("customerName")), "%" + customerName.toLowerCase() + "%");
    }

    public Specification<Customers> filterByAddress(String customerAddress){
        return (root, query, builder) -> builder.like(builder.lower(root.get("customerAddress")), "%" + customerAddress.toLowerCase() + "%");
    }

    public Specification<Customers> filterSpec(String filterBy, String filterValue){
        Map<String, Specification<Customers>> filters = new HashMap<>();
        filters.put("customerName", filterByName(filterValue));
        filters.put("customerAddress", filterByAddress(filterValue));
        
        return filters.get(filterBy);
    }

    public Comparator<Customers> sortComparatorSpec(String sortBy){
        Map<String, Comparator<Customers>> sorts = new HashMap<>();
        sorts.put("Name asc", Comparator.comparing(Customers::getCustomerName));
        sorts.put("Name desc", Comparator.comparing(Customers::getCustomerName).reversed());
        sorts.put("Address asc", Comparator.comparing(Customers::getCustomerAddress));
        sorts.put("Address desc", Comparator.comparing(Customers::getCustomerAddress).reversed());
        sorts.put("Last Order Date asc", Comparator.comparing(Customers::getLastOrderDate));
        sorts.put("Last Order Date desc", Comparator.comparing(Customers::getLastOrderDate).reversed());

        return sorts.get(sortBy);
    }
}
