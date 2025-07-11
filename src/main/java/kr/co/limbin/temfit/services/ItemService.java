package kr.co.limbin.temfit.services;

import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.mappers.ItemMapper;
import kr.co.limbin.temfit.mappers.PaymentMapper;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final PaymentMapper paymentMapper;
    private final ItemMapper itemMapper;

    public PaymentEntity getById(int id) {
        if (id < 1) {
            return null;
        }
        return this.paymentMapper.selectById(id);
    }

    public Result paymentWrite(UserEntity user, PaymentEntity payment) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        if (payment == null
                || !ArticleService.isContentValid(payment.getDeliveryContent())) {
            return CommonResult.FAILURE;
        }

        payment.setId(1); //하드코딩
        payment.setUserEmail(user.getEmail());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setModifiedAt(null);
        payment.setDeleted(false);

        return this.paymentMapper.insert(payment) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
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

    public BrandEntity[] getBrandALl() {
        return this.itemMapper.selectBrandAll();
    }

    public SkinEntity[] getSkinALl() {
        return this.itemMapper.selectSkinAll();
    }
}
