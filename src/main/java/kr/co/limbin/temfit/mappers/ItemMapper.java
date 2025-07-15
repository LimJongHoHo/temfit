package kr.co.limbin.temfit.mappers;

import kr.co.limbin.temfit.entities.BrandEntity;
import kr.co.limbin.temfit.entities.ProductEntity;
import kr.co.limbin.temfit.entities.SkinEntity;
import kr.co.limbin.temfit.vos.CartDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ItemMapper {
    int insert(@Param(value = "product") ProductEntity product);

    ProductEntity getByProductId(@Param(value = "id") int id);

    ProductEntity[] getProductAll();

    BrandEntity getByBrandId(@Param(value = "id") int id);

    BrandEntity[] selectBrandAll();

    SkinEntity[] selectSkinAll();

    CartDetailVo[] getByCartId(@Param(value = "cartId") int cartId);
}
