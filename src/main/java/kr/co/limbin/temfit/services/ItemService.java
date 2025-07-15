package kr.co.limbin.temfit.services;

import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.mappers.ItemMapper;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.vos.CartDetailVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemMapper itemMapper;

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

    public ProductEntity[] getProductAll() {
        return this.itemMapper.getProductAll();
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
}
