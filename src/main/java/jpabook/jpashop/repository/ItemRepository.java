package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom{
    // 쿼리 메소드: find + (엔티티이름) + By+ 변수이름
    // 조건 하나 추가할 때 마다 By

    // 상품명으로 데이터 조회하기
    List<Item> findByItemName(String itemName);

    // or 조건
    List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail);

    // lessThan 조건
    List<Item> findByPriceLessThan(Integer price);

    // orderBy 조건
    List<Item> findAllByOrderByPriceDesc();

    // orderBy 조건 + 가격 조건
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // nativeQuery
    // 기존의 db 에서 사용하던 쿼리를 그대로 사용해야 할 때 복잡한 쿼리를 그대로 사용 가능 (데이터베이스 종속적)
    @Query(value = "select * from item i where i.item_content like %:itemContent% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

}
