package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {


//    @Value("${file.dir}")
//    private String fileDir;

    private final ItemRepository itemRepository;



    @Transactional
    public void saveItem(Item item, MultipartFile imgFile) throws IOException {
        String oriImgName = imgFile.getOriginalFilename();
        String imgName = "";

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/";
        // UUID 를 이용하여 파일명 새로 생성
        UUID uuid = UUID.randomUUID();
        String savedFileName = uuid + "_" + oriImgName; // 파일명 -> imgName
        imgName = savedFileName;
        File saveFile = new File(projectPath, imgName);
        imgFile.transferTo(saveFile);
        item.setImgName(imgName);
        item.setImgPath("/files/" + imgName);

        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item item = itemRepository.findOne(itemId);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
    }



    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
