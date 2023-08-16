package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Getter
@Setter
public class ItemImg {

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String oriImgName; //원본 이미지 파일명
    private String imgName; //상품 이미지 파일명
    private String imgPath; // 이미지 조회 경로
    
    private  String repimgYn; // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 상품 엔티티와 다대일 단방향 관계로 매핑 (상품1개 -> 사진 여러개)

//    추후 이미지 수정시 사용
    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgPath = imgUrl;
    }
}
