package kr.co.limbin.temfit.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class CartEntity {
    private int id;
    private String userEmail;
    private String creatUserEmail;
    private String modifyUserEmail;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isPaid;
}
