package kr.co.limbin.temfit.mappers;

import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.vos.BrandVo;
import kr.co.limbin.temfit.vos.CartDetailVo;
import kr.co.limbin.temfit.vos.ProductVo;
import kr.co.limbin.temfit.vos.SkinVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ItemMapper {
    int deleteCartDetail(@Param(value = "cartDetailId") int cartDetailId);

    int deleteIngredient(@Param(value = "ingredientId") int ingredientId);

    int updateCartDetail(@Param(value = "cartDetail") CartDetailEntity cartDetail);

    int insert(@Param(value = "product") ProductEntity product);

    int insertIngredient(@Param(value = "ingredient") IngredientEntity ingredient);

    ProductEntity getByProductId(@Param(value = "id") int id);

    ProductVo[] getProductAll();

    BrandEntity getByBrandId(@Param(value = "id") int id);

    SkinEntity getBySkinId(@Param(value = "id") int id);

    BrandVo[] selectBrandAll();

    SkinVo[] selectSkinAll();

    CartDetailEntity getCartDetail(@Param(value = "cartDetailId") int cartDetailId);

    CartDetailVo[] getByCartId(@Param(value = "cartId") int cartId);

    int insertCart(@Param(value = "cart") CartEntity cart);

    int insertCartDetail(@Param(value = "cartDetail") CartDetailEntity cartDetail);

    Integer getCartId(@Param(value = "userEmail")  String userEmail);

    CartDetailEntity getQuantity(@Param(value = "cartDetail") CartDetailEntity cartDetail);

    ProductVo[] getProductBySkinId(@Param(value = "skinId") int skinId);

    ProductVo[] getProductByBrandId(@Param(value = "brandId") int brandId);

}
