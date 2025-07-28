package kr.co.limbin.temfit.services;

import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.mappers.ItemMapper;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.results.ResultTuple;
import kr.co.limbin.temfit.vos.BrandVo;
import kr.co.limbin.temfit.vos.CartDetailVo;
import kr.co.limbin.temfit.vos.ProductVo;
import kr.co.limbin.temfit.vos.SkinVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemMapper itemMapper;

    public Result deleteCartDetail(int cartDetailId) {
        if (cartDetailId <1) {
            return CommonResult.FAILURE;
        }

        return this.itemMapper.deleteCartDetail(cartDetailId) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result updateCartDetail(int cartDetailId, String calc) {
        if (cartDetailId <1) {
            return CommonResult.FAILURE;
        }

        if (calc == null ||
                (!calc.equals("plus") && !calc.equals("minus"))) {
            return CommonResult.FAILURE;
        }

        CartDetailEntity dbCartDetail = this.itemMapper.getCartDetail(cartDetailId);

        if (calc.equals("plus")) {
            dbCartDetail.setQuantity(dbCartDetail.getQuantity() + 1);
        }
        if (calc.equals("minus")) {
            dbCartDetail.setQuantity(dbCartDetail.getQuantity() - 1);
        }

        return this.itemMapper.updateCartDetail(dbCartDetail) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result update(UserEntity signedUser, ProductEntity product) {
        if (signedUser == null
                || signedUser.isDeleted()
                || signedUser.isSuspended()) {
            return CommonResult.FAILURE;
        }

        if (product == null
                || product.getImageUrl() == null
                || product.getName() == null
                || product.getSize() == null) {
            return CommonResult.FAILURE;
        }

        ProductEntity dbProduct = this.itemMapper.getByProductId(product.getId());

        dbProduct.setImageUrl(product.getImageUrl());
        dbProduct.setName(product.getName());
        dbProduct.setBrandId(product.getBrandId());
        dbProduct.setSkinId(product.getSkinId());
        dbProduct.setSize(product.getSize());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setDiscountRate(product.getDiscountRate());
        dbProduct.setDeliveryFee(product.getDeliveryFee());
        dbProduct.setDeliveryCompany(product.getDeliveryCompany());
        dbProduct.setDeliveryAdd(product.getDeliveryAdd());
        dbProduct.setModifyUserEmail(signedUser.getEmail());
        dbProduct.setModifiedAt(LocalDateTime.now());
        dbProduct.setDeleted(false);
        return this.itemMapper.update(dbProduct) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public ResultTuple<ProductEntity> insert(UserEntity signedUser, ProductEntity product) {
        if (signedUser == null
                || signedUser.isDeleted()
                || signedUser.isSuspended()) {
            return ResultTuple.<ProductEntity>builder().result(CommonResult.FAILURE).build();
        }

        if (product == null
                || product.getImageUrl() == null
                || product.getName() == null
                || product.getSize() == null) {
            return ResultTuple.<ProductEntity>builder().result(CommonResult.FAILURE).build();
        }

        product.setCreatUserEmail(signedUser.getEmail());
        product.setCreatedAt(LocalDateTime.now());
        product.setDeleted(false);
        return this.itemMapper.insert(product) > 0 ? ResultTuple.<ProductEntity>builder().result(CommonResult.SUCCESS).payload(product).build() : ResultTuple.<ProductEntity>builder().result(CommonResult.FAILURE).build();
    }

    public ProductVo[] getProductBySkinId(int skinId) {
        return this.itemMapper.getProductBySkinId(skinId);
    }

    public ProductVo[] getProductByBrandId(int brandId) {
        return this.itemMapper.getProductByBrandId(brandId);
    }

    public ProductEntity getByProductId(int id) {
        return this.itemMapper.getByProductId(id);
    }

    public BrandEntity getByBrandId(int id) {
        return this.itemMapper.getByBrandId(id);
    }
    public SkinEntity getBySkinId(int id) {
        return this.itemMapper.getBySkinId(id);
    }

    public ArticleEntity getArticleIdByProductId (int productId)
    {
        return this.itemMapper.getArticleId(productId);
    }

    public ProductVo[] getProductAll() {
        return this.itemMapper.getProductAll();
    }

    public BrandVo[] getBrandALl() {
        return this.itemMapper.selectBrandAll();
    }

    public SkinVo[] getSkinALl() {
        return this.itemMapper.selectSkinAll();
    }

    public CartDetailVo[] getByCartId(int cartId) {
        return this.itemMapper.getByCartId(cartId);
    }

    public Integer getCartId(String userEmail) {
        return this.itemMapper.getCartId(userEmail);
    }

    public Integer insertCart(UserEntity signedUser) {
        if (signedUser == null ||  signedUser.isDeleted() || signedUser.isSuspended()) {
            return null;
        }

        CartEntity cart = new CartEntity();

        cart.setUserEmail(signedUser.getEmail());
        cart.setCreatUserEmail(signedUser.getEmail());
        cart.setCreatedAt(LocalDateTime.now());
        cart.setPaid(false);

        return this.itemMapper.insertCart(cart) > 0 ? -1 : null;
    }

    public Result insertCartDetail(CartDetailEntity cartDetail) {
        if (cartDetail == null) {
            return CommonResult.FAILURE;
        }

        cartDetail.setCreatedAt(LocalDateTime.now());

        CartDetailEntity dbCartDetail = this.itemMapper.getQuantity(cartDetail);

        if (dbCartDetail == null) {
            cartDetail.setQuantity(1);
            return this.itemMapper.insertCartDetail(cartDetail) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
        } else {
            cartDetail.setId(dbCartDetail.getId());
            cartDetail.setQuantity(dbCartDetail.getQuantity() + 1);
            return this.itemMapper.updateCartDetail(cartDetail) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
        }
    }

    public Result insertIngredient(UserEntity signedUser, IngredientEntity ingredient) {
        ingredient.setCreatUserEmail(signedUser.getEmail());
        ingredient.setCreatedAt(LocalDateTime.now());
        return this.itemMapper.insertIngredient(ingredient) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result deleteIngredient(int productId) {
        if (productId < 1) {
            return CommonResult.FAILURE;
        }

        if (this.itemMapper.deleteIngredient(productId) == 0) {
            return CommonResult.SUCCESS;
        }

        return this.itemMapper.deleteIngredient(productId) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public IngredientEntity[] getIngredientByProductId(int productId) {
        return this.itemMapper.getIngredientByProductId(productId);
    }

}
