package kr.co.limbin.temfit.mappers;

import kr.co.limbin.temfit.entities.ReviewEntity;
import kr.co.limbin.temfit.vos.ReviewVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewMapper {
    int insert(@Param(value = "review") ReviewEntity review);

    ReviewVo[] selectByAll(@Param(value = "articleId") int articleId);

    ReviewVo[] selectTwoReview(@Param(value = "articleId") int articleId);

    ReviewVo selectByReviewId(@Param(value = "id") int id);

    int countReview(@Param(value = "articleId") int articleId);

    int update(@Param(value = "review") ReviewVo review);

}
