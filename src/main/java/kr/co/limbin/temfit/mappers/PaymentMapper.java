package kr.co.limbin.temfit.mappers;

import kr.co.limbin.temfit.entities.PaymentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface PaymentMapper {
    int insert(@Param(value = "payment") PaymentEntity payment);

    PaymentEntity selectById(@Param(value = "id") int id);
}
