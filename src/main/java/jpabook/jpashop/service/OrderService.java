package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.constant.DeliveryStatus;

import jpabook.jpashop.dto.OrderDto;
import jpabook.jpashop.dto.OrderHistDto;
import jpabook.jpashop.dto.OrderItemDto;
import jpabook.jpashop.repository.ItemImgRepository;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(OrderDto orderDto, String memid) {
        //엔티티 조회
        Item item = itemRepository.findById(orderDto.getItemId()) // 주문할 상품을 조회
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findById(memid);

        List<OrderItem> orderItemList = new ArrayList<>();

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // item: 주문할 상품 엔티티, .getCount(): 주문 수량
        orderItemList.add(orderItem);

        //주문 생성 & 저장
//        Order order = Order.createOrder(member, delivery, orderItem); //delivery는 추후에 다시 해보자
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        //주문 취소
        order.cancel();
    }


    /**
     * 주문 목록을 조회
     */
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable); // 주문 목록을 조회
        Long totalCount = orderRepository.countOrder(email); // 주문 총 개수

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) { // entity -> dto

                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y"); // 대표상품인지 확인
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgPath()); // entity-> dto
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    /**
     * validateOrder (DB 에 있는 id와 주문자 id를 대조)
     */
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email); // 유저 이메일 받아옴
        Order order = orderRepository.findById(orderId) // 주문 데이터 받아오고
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember(); // 위의 주문을 한 유저의 정보를 받아옴

        if(!StringUtils.equals(curMember.getMemId(), savedMember.getMemId())){
            return false; // 그 둘이 같지 않으면은 false
        }
        return true; // 같으면 true
    }

    /**
     * 주문생성 (장바구니 -> 상품 데이터 받아오기)
     */
    public Long orders(List<OrderDto> orderDtoList, String memid){

        Member member = memberRepository.findByEmail(memid);
        List<OrderItem> orderItemList = new ArrayList<>();

        // 주문할 상품 리스트
        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList); // 회원이랑 주문할 상품 리스트들을 주문에 담음
        orderRepository.save(order);

        return order.getId();
    }

    //검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAllByString(orderSearch);
//    }
}
