package kr.co.limbin.temfit.services;

import kr.co.limbin.temfit.entities.PaymentEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.mappers.PaymentMapper;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMapper paymentMapper;

    public Result paymentWrite(UserEntity user, PaymentEntity payment) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        if (payment == null) {
            return CommonResult.FAILURE;
        }

        payment.setUserEmail(user.getEmail());
        payment.setCreatUserEmail(user.getEmail());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setDeleted(false);

        return this.paymentMapper.insert(payment) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public PaymentEntity getById(int id) {
        if (id < 1) {
            return null;
        }
        return this.paymentMapper.selectById(id);
    }

    public Result deleteProductById(int productId) {
        if (productId < 1) {
            return CommonResult.FAILURE;
        }
        return this.paymentMapper.deleteProduct(productId) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }


}
