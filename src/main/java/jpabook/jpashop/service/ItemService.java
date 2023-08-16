package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.ItemImg;
import jpabook.jpashop.dto.ItemFormDto;
import jpabook.jpashop.dto.ItemImgDto;
import jpabook.jpashop.dto.ItemSearchDto;
import jpabook.jpashop.dto.MainItemDto;
import jpabook.jpashop.repository.ItemImgRepository;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {


//    @Value("${file.dir}")
//    private String fileDir;

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;


    @Transactional
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> imgFileList) throws Exception {

        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i=0;i<imgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0)
                itemImg.setRepimgYn("Y"); // 첫번째 이미지를 대표 상품 이미지로 설정
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, imgFileList.get(i)); // 리스트 형태로 이미지들 저장
        }
        return item.getId();
    }


    // 상품이랑, 상품이미지의 entity -> dto 로 바꾸기만 하는 service
    @Transactional(readOnly = true) // 트랜젝션을 readOnly 로 설정할 경우, JPA 가 변경감지(더티체킹)를 수행하지 않아서 성능 향상됨_데이터 수정이 일어나지 않기 때문에
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto); // entity -> dto
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto; //itemFormDto 에는 상품이랑 상품 이미지 다 있움
    }

//    상품수정
public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
    Item item = itemRepository.findById(itemFormDto.getId()) // 상품 아이디로 상품 엔티티를 조회 (아이디는 상품 등록 화면에서 전달 받음)
            .orElseThrow(EntityNotFoundException::new);

    item.updateItem(itemFormDto); // itemFormDto 로 상품 엔티티 수정 (itemFormDto 는 상품 등록 화면에서 전달 받음)

    List<Long> itemImgIds = itemFormDto.getItemImgIds(); // 상품 이미지 아이디 리스트 조회

    //이미지 등록
    for(int i=0;i<itemImgFileList.size();i++){
        itemImgService.updateItemImg(itemImgIds.get(i),
                itemImgFileList.get(i));
    }

    return item.getId();
}

//  상품 조회
    @Transactional(readOnly = true) // 트랜젝션을 readOnly 로 설정할 경우, JPA 가 변경감지(더티체킹)를 수행하지 않아서 성능 향상됨 _데이터 수정이 일어나지 않기 때문에
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    // 메인 페이지에 보여줄 상품 데이테 조회
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

}
