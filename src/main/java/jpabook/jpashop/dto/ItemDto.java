package jpabook.jpashop.dto;

import jpabook.jpashop.domain.constant.ItemSellStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ItemDto {
    private Long id;
    private String itemName;
    private Integer price;
    private String itemContent;

    private String sellStatCd;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}


//    private int stockQuantity; //상품 재고수량
//

//
//    @Enumerated(EnumType.STRING)
//    private ItemSellStatus itemSellStatus; //상품 판매 상태