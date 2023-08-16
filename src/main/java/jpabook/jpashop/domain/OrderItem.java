package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //한 아이템은 여러 주문테이블에 들어갈 수 있다.
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) //한 주문에 여러 아이템이 들어갈 수 있다.
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 가격
    private int count; //주문 수량


    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
//        현재 시간 기준으로 상품가격=주문가격 (상품가격은 관리자가 세팅하는 값에 따라 달라지니까
//        현재 주문한 시간으로 정해둬야 한다.
        orderItem.setOrderPrice(item.getPrice());

//        현재 재고 수량에서 주문한만큼을 빼야한다.
        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//

    /**
     * 주문 취소 시 주문 수량을 상품의 재고에 더해주는 로직
     */
    public void cancel() {
        getItem().addStock(count);
    }

    //==조회 로직==//

    /**
     * 주문상품 전체 가격 조회 로직
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
