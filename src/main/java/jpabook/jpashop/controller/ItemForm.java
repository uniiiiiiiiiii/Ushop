package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemForm {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    private String oriImgName; //원본 이미지 파일명
    private String imgName; //상품 이미지 파일명
    private String imgPath; // 이미지 조회 경로

//    private MultipartFile attachFile;
//    private List<ItemImg> imageFiles;

//    private String author;
//    private String isbn;
}
