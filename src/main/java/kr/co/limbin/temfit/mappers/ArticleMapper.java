package kr.co.limbin.temfit.mappers;

import kr.co.limbin.temfit.entities.ArticleCoverEntity;
import kr.co.limbin.temfit.entities.ArticleEntity;
import kr.co.limbin.temfit.vos.ArticleVo;
import kr.co.limbin.temfit.vos.PageVo;
import kr.co.limbin.temfit.vos.SearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {
    int insert(@Param(value = "article") ArticleEntity article);

    int insertCover(@Param(value = "cover") ArticleCoverEntity cover);

    int selectCountBySearch(@Param(value = "searchVo") SearchVo searchVo);

    ArticleVo[] selectBySearch(@Param(value = "pageVo") PageVo pageVo, @Param(value = "searchVo") SearchVo searchVo);

    ArticleEntity selectById(@Param(value = "id") int id);

    ArticleCoverEntity selectByIdCover(@Param(value = "id") int id);

    int update(@Param(value = "article") ArticleEntity article);

    int updateCover(@Param(value = "cover") ArticleCoverEntity cover);
}
