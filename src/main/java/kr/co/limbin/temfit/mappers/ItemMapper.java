package kr.co.limbin.temfit.mappers;

import kr.co.limbin.temfit.entities.BrandEntity;
import kr.co.limbin.temfit.entities.ProductEntity;
import kr.co.limbin.temfit.entities.SkinEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ItemMapper {
    int insert(@Param(value = "product") ProductEntity product);

    BrandEntity[] selectBrandAll();

    SkinEntity[] selectSkinAll();
}
