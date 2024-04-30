package cau.capstone.backend.global;

import java.util.List;
import java.util.stream.Collectors;

public class ImageDto {

    private Long imageId;
    private String imageUrl;

    // Getters and setters
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public ImageDto entityToDto(Image entity) {
        ImageDto dto = new ImageDto();
        dto.setImageId(entity.getImageId());
        dto.setImageUrl(entity.getImageUrl());
        return dto;
    }

    public Image dtoToEntity(ImageDto dto) {
        Image entity = new Image();
        entity.setImageId(dto.getImageId());
        entity.setImageUrl(dto.getImageUrl());
        return entity;
    }

    public List<ImageDto> entitiesToDtos(List<Image> entities) {
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public List<Image> dtosToEntities(List<ImageDto> dtos) {
        return dtos.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }

}