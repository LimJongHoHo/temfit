package kr.co.limbin.temfit.vos;

import kr.co.limbin.temfit.entities.ArticleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArticleVo extends ArticleEntity {
    private String userNickname;
}
