package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.goods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 종 주문 2개
 * * userA
 * 	 * JPA1 BOOK
 * 	 * JPA2 BOOK
 * * userB
 * 	 * SPRING1 BOOK
 * 	 * SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            System.out.println("Init1" + this.getClass());
            Member member = createMember("userA", "서울", "1", "1111");
            em.persist(member);

            goods item11 = createBook("Test1", 10000, 100);
            em.persist(item11);

            goods item12 = createBook("Test2", 20000, 100);
            em.persist(item12);

            OrderItem orderItem1 = OrderItem.createOrderItem(item11, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(item12, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "진주", "2", "2222");
            em.persist(member);

            goods item11 = createBook("Test3", 30000, 200);
            em.persist(item11);

            goods item12 = createBook("Test4", 40000, 300);
            em.persist(item12);

            goods item13 = createBook("Test5", 40000, 300);
            em.persist(item12);

            goods item14 = createBook("Test6", 40000, 300);
            em.persist(item12);

            goods item15 = createBook("Test7", 40000, 300);
            em.persist(item12);

            OrderItem orderItem1 = OrderItem.createOrderItem(item11, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(item12, 40000, 4);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private goods createBook(String name, int price, int stockQuantity) {
            goods item11 = new goods();
            item11.setName(name);
            item11.setPrice(price);
            item11.setStockQuantity(stockQuantity);
            return item11;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}

