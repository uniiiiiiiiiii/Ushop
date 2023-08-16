package jpabook.jpashop.dto;

import jpabook.jpashop.domain.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 상품 데이터 조회 DTO
public class ItemSearchDto {
    //현재 시간과 상품 등록일을 비교해서 상품 데이터 조회
    private  String searchDataType;
    /*
    all: 상품 등록일 전체
    1d/1w/1m/6m: 최근 하루/1주/한달/6개월 동안 등록된 상품
     */

    // 상품의 판매상태를 기준으로 상품 데이터를 조회
    private ItemSellStatus searchSellStatus;

    // 상품을 조회할 때 어떤 유형으로 조회할 지 선택
    private String searchBy;
    /*
    itemName: 상품명
    createBy: 상품 등록자 아이디
     */

    // 조회할 검색어를 저장할 변수
    private String searchQuery = "";
    /*
    itemNm: 상품명 기준 검색
    createBy: 상품 등록자 아이디 검색
     */

}
