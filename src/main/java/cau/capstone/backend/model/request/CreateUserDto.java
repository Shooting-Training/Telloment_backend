package cau.capstone.backend.model.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    private String name;
    private String keyCode;
    private String image;
    private int age;


    public static CreateUserDto of(String name, String keyCode, String image, int age) {
        return new CreateUserDto(name, keyCode, image, age);
    }
}
