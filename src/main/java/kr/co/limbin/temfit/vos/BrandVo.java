package kr.co.limbin.temfit.vos;

import kr.co.limbin.temfit.entities.BrandEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BrandVo extends BrandEntity {
    private int num;
}
