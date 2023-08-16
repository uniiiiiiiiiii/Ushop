package jpabook.jpashop.dto;


import jpabook.jpashop.domain.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgPath;

    private String repImgYn;

    // entity <-> dto 과정이 복잡해서 modelMapper 라이브러리를 사용하기로 결정
    private static ModelMapper modelMapper = new ModelMapper();

    // ModelMapper: 서로 다른 클래스의 값을 필드의 이름과 자료형이 같을 때 getter, setter 를 통해 값을 복사해서 객체를 반환함
    // entity 를 파라미터로 받아서 dto 로 반환
    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg,ItemImgDto.class);
    }
}
