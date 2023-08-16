package jpabook.jpashop.service;

import jpabook.jpashop.domain.ItemImg;
import jpabook.jpashop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    @Value("${itemImgLocation}") // application.properties 에 적었던 itemImgLocation 값을 불러와서 itemImgLocation 변수에다가 넣어줌
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{

        // 일단 비어있는 변수 만들어두고 밑에서 넣어줌
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgPath = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){

            // 상품 이미지 이름 = 저장할 경로 + 파일이름 + 파일크기(byte)
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());

            // 저장한 상품 이미지를 불러올 경로
            imgPath = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgPath);
        itemImgRepository.save(itemImg);
    }

    // 상품 이미지 수정
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{

        if(!itemImgFile.isEmpty()){ // 상품 이미지를 수정한 경우, 상품 이미지를 업데이트함
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId) // 상품 이미지 아이디를 이용해서 기존에 저장했던 상품 이미지 엔티티를 조회
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제 (수정했으니까 원래 파일은 삭제)
            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation+"/"+
                        savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // 업데이트한 상품 이미지 파일을 업로드

            // 변경된 상품 이미지 정보를 setting_updateItemImg 메소드를 이용함!
            String imgPath = "/img/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgPath);

            /*
            이미지를 수정할때 updateItemImg 을 쓰는 이유는
            savedItemImg 엔티티는 현재 영속 상태이므로 => 데이터 변경하는거로도 변경 감지 기능이 동작해서 transaction 이 끝날 때 update 쿼리가 실행
             */
        }
    }
}
