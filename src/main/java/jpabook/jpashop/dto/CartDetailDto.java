package jpabook.jpashop.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailDto {
    private Long cartItemId; //장바구니 상품 아이디
    private String itemName; //상품명
    private int price; //상품 금액
    private int count; //수량
    private String imgPath; //상품 이미지 경로
}
