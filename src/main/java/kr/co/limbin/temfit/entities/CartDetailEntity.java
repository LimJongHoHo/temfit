package kr.co.limbin.temfit.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class CartDetailEntity {
    private int id;
    private int cartId;
    private int productId;
    private int articleId;
    private int quantity;
    private String creatUserEmail;
    private LocalDateTime createdAt;
}
