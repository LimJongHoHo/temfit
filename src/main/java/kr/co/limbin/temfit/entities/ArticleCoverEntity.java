package kr.co.limbin.temfit.entities;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "articleId")
public class ArticleCoverEntity {
    private int articleId;
    private String coverUrl1;
    private String coverUrl2;
    private String coverUrl3;
    private String coverUrl4;
    private String coverUrl5;
    private String coverUrl6;
    private String coverUrl7;
    private String coverUrl8;
    private LocalDateTime createdAt;
}