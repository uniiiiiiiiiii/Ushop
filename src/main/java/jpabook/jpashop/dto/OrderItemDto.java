package jpabook.jpashop.dto;

import jpabook.jpashop.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private String itemNm; //상품명
    private int count; //주문 수량
    private int orderPrice; //주문 금액
    private String imgPath; //상품 이미지 경로

    public OrderItemDto(OrderItem orderItem, String imgPath){ // 주문상품, 이미지경로를 파라미터로 받음
        this.itemNm = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgPath = imgPath;
    }
}
