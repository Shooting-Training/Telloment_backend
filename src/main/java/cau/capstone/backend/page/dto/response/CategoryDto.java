package cau.capstone.backend.page.dto.response;

public class CategoryDto {
    private String code;
    private String name;

    private int bookCount;

    public CategoryDto(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBookCount(int num) {this.bookCount = bookCount; }
}