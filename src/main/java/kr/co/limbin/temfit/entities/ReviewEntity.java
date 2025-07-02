package kr.co.limbin.temfit.entities;

import java.time.LocalDateTime;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ReviewEntity {
    private int id;
    private int articleId;
    private String userEmail;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isDeleted;
}
