package kr.co.limbin.temfit.vos;

import kr.co.limbin.temfit.entities.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewVo extends ReviewEntity {
    private String nickname;
}
