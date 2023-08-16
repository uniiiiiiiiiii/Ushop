package jpabook.jpashop.domain;

import jpabook.jpashop.domain.constant.ItemSellStatus;
import jpabook.jpashop.dto.ItemFormDto;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemName; //상품명
    @Column(nullable = false)
    private int price; //상품가격
    @Column(nullable = false)
    private int stockQuantity; //상품 재고수량

    @Lob
    @Column(nullable = false)
    private String itemContent; //상품내용 (설명)

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태


//    다대다 매핑을 하면 안된다는데 다른방법을 구상해보자.
//    @ManyToMany(mappedBy = "items")
//    private List<Category> categories = new ArrayList<>();


    //==비즈니스 로직==//
        /*
    엔티티 클래스에 비즈니스 로직을 추가하면 객체지향적으로 코딩을 할 수 있고, 코드를 재활용 할 수도 있다.
    데이터 변경 포인트를 여기로 지정할 수 있움(데이터 수정 변경 여기서만 한다는 뜻)
    엔티티 클래스에 비즈니스 로직(데이터 처리) 을 추가하면
    1. 객체지향적으로 코딩을 할 수 있고,
    2. 코드를 재활용 할 수도 있다.
    3. 데이터 변경 포인트를 여기로 지정할 수 있움(데이터 수정 변경 여기서만 한다는 뜻)
     */


    /**
     * 상품 데이터 업데이트
     */
    public void updateItem(ItemFormDto itemFormDto){
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockQuantity = itemFormDto.getStockQuantity();
        this.itemContent = itemFormDto.getItemContent();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock!!");
        }
        this.stockQuantity = restStock;
    }
}
