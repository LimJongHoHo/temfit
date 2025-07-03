package kr.co.limbin.temfit.mappers;

import kr.co.limbin.temfit.entities.ArticleEntity;
import kr.co.limbin.temfit.entities.ReviewEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewMapper {
    int insert(@Param(value = "review") ReviewEntity review);

    ReviewEntity selectById(@Param(value = "articleId") int articleId);




}
