package com.example.btpn.services.specifications;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.btpn.models.Customers;
import com.example.btpn.models.Items;
import com.example.btpn.models.Orders;

@Component
public class OrderSpec {
    public Specification<Orders> filterByCustomerName(String customerName){
        return (root, query, builder) -> builder.like(builder.lower(root.get("customers").get("customerName")), "%" + customerName.toLowerCase() + "%");
    }

    public Specification<Orders> filterByItemName(String itemName){
        return (root, query, builder) -> builder.like(builder.lower(root.get("items").get("itemName")), "%" + itemName.toLowerCase() + "%");
    }

    public Specification<Orders> filterSpec(String filterBy, String filterValue){
        Map<String, Specification<Orders>> filters = new HashMap<>();
        filters.put("customerName", filterByCustomerName(filterValue));
        filters.put("itemName", filterByItemName(filterValue));
        
        return filters.get(filterBy);
    }

    public Comparator<Orders> sortComparatorSpec(String sortBy){
        Map<String, Comparator<Orders>> sorts = new HashMap<>();
        sorts.put("Customer Name asc", Comparator.comparing(Orders::getCustomers, Comparator.comparing(Customers::getCustomerName)));
        sorts.put("Customer Name desc", Comparator.comparing(Orders::getCustomers, Comparator.comparing(Customers::getCustomerName).reversed()));
        sorts.put("Item Name asc", Comparator.comparing(Orders::getItems, Comparator.comparing(Items::getItemName)));
        sorts.put("Item Name desc", Comparator.comparing(Orders::getItems, Comparator.comparing(Items::getItemName).reversed()));
        sorts.put("Total Price asc", Comparator.comparing(Orders::getTotalPrice));
        sorts.put("Total Price desc", Comparator.comparing(Orders::getTotalPrice).reversed());
        sorts.put("Order Date asc", Comparator.comparing(Orders::getOrderDate));
        sorts.put("Order Date desc", Comparator.comparing(Orders::getOrderDate).reversed());

        return sorts.get(sortBy);
    }
}
