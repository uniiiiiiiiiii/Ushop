package jpabook.jpashop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {

    private Long id;
    private String itemName;
    private String itemContent;
    private String imgPath;
    private Integer price;

    @QueryProjection // dto로 객체를 변환
    public MainItemDto(Long id, String itemName, String itemContent, String imgPath,Integer price){
        this.id = id;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.imgPath = imgPath;
        this.price = price;
    }

}
