package com.hcmut.ecommerce.domain.order.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CreateGhtkOrderRequest {
    private Order order;
    private List<Product> products;

    @Data
    public static class Order {
        private String id;
        private String pick_name;
        private String pick_address;
        private String pick_province;
        private String pick_district;
        private String pick_ward;
        private String pick_tel;
        private String tel;
        private String name;
        private String address;
        private String province;
        private String district;
        private String ward;
        private String hamlet;
        private String transport;  
        private Integer pick_money;
        private Integer value;      
        private String note;
        private String is_freeship; 
    }

    @Data
    public static class Product {
        private String name;
        private Double weight;   
        private Integer quantity;
        private String product_code;
    }
}


