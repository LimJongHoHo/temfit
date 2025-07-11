package kr.co.limbin.temfit.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ProductEntity {
    private int id;
    private String imageUrl;
    private String name;
    private int brandId;
    private int skinId;
    private String size;
    private int price;
    private int discountRate;
    private int deliveryFee;
    private String deliveryCompany;
    private String deliveryAdd;
    private String creatUserEmail;
    private String modifyUserEmail;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isDeleted;
}
