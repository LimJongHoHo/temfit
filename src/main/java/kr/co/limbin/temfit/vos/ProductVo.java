package kr.co.limbin.temfit.vos;

import kr.co.limbin.temfit.entities.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductVo extends ProductEntity {
    private String brandName;
    private String skinName;
    private int num;
}
