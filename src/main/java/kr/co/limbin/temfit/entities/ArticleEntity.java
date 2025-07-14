package kr.co.limbin.temfit.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ArticleEntity {
    private int id;
    private int productId;
    private String userEmail;
    private String title;
    private String content;
    private int view;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isDeleted;
}
