package kr.co.limbin.temfit.mappers;

import kr.co.limbin.temfit.entities.ReviewEntity;
import kr.co.limbin.temfit.vos.ReviewVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewMapper {
    int insert(@Param(value = "review") ReviewEntity review);

    ReviewVo[] selectByAll(@Param(value = "articleId") int articleId);

    ReviewVo selectByReviewId(@Param("id") int id);

    int update(@Param(value = "review") ReviewEntity review);

}
