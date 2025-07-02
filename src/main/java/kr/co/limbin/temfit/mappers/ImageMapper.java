package kr.co.limbin.temfit.mappers;

import kr.co.limbin.temfit.entities.ImageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImageMapper {
    int insert(@Param(value = "image") ImageEntity image);

    ImageEntity selectById(@Param(value = "id") int id);
}
