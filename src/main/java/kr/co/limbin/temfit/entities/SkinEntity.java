package kr.co.limbin.temfit.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class SkinEntity {
    private int id;
    private String name;
    private String creatUserEmail;
    private String modifyUserEmail;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isDeleted;
}
