package kr.co.limbin.temfit.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class IngredientEntity {
    private int id;
    private String korName;
    private String score;
    private int productId;
    private String creatUserEmail;
    private LocalDateTime createdAt;
}
