package cau.capstone.backend.global;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "image_info")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_url")
    private String imageUrl;

    // JPA 요구 사항에 따라 기본 생성자를 제공합니다.
    protected Image() { }

    // 모든 값을 인자로 받는 생성자.
    public Image(Long imageId, String imageUrl) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }

    // Getter 메소드들
    public Long getImageId() {
        return imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // DTO를 엔티티로 변환하는 정적 메소드
    public static Image dtoToEntity(ImageDto dto) {
        return new Image(dto.getImageId(), dto.getImageUrl());
    }

    // DTO 리스트를 엔티티 리스트로 변환하는 정적 메소드
    public static List<Image> dtoListToEntityList(List<ImageDto> dtoList) {
        if (dtoList == null) {
            return null;
        }
        List<Image> entityList = new ArrayList<>();
        for (ImageDto dto : dtoList) {
            entityList.add(dtoToEntity(dto));
        }
        return entityList;
    }
}