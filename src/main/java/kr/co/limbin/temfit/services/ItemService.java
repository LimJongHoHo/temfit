package kr.co.limbin.temfit.services;

import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.mappers.ItemMapper;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.vos.CartDetailVo;
import kr.co.limbin.temfit.vos.ProductVo;
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

    public Result insert(UserEntity signedUser, ProductEntity product) {
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

        product.setCreatUserEmail(signedUser.getEmail());
        product.setCreatedAt(LocalDateTime.now());
        product.setDeleted(false);
        return this.itemMapper.insert(product) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
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

    public ProductVo[] getProductAll() {
        return this.itemMapper.getProductAll();
    }

    public ProductVo[] getProductBySkinAll(){
        return this.itemMapper.getProductBySkinAll();
    }

    public ProductVo[] getProductByBrandAll(){
        return this.itemMapper.getProductByBrandAll();
    }

    public BrandEntity[] getBrandALl() {
        return this.itemMapper.selectBrandAll();
    }

    public SkinEntity[] getSkinALl() {
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

    public ProductVo[] getProductByBrandId(int brandId) {
        return this.itemMapper.getProductByBrandId(brandId);
    }

    public ProductVo[] getProductBySkinId(int skinId) {
        return this.itemMapper.getProductBySkinId(skinId);
    }

}
