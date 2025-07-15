package kr.co.limbin.temfit.vos;

import kr.co.limbin.temfit.entities.CartDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDetailVo extends CartDetailEntity {
    private String productImage;
    private String productName;
    private int productPrice;
    private String brandName;
    private String skinName;
    private int deliveryFee;
    private String deliveryCompany;
}