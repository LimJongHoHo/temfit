package kr.co.limbin.temfit.services;

import kr.co.limbin.temfit.entities.ArticleCoverEntity;
import kr.co.limbin.temfit.entities.ArticleEntity;
import kr.co.limbin.temfit.entities.ReviewEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.mappers.ArticleMapper;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.vos.ArticleVo;
import kr.co.limbin.temfit.vos.PageVo;
import kr.co.limbin.temfit.vos.SearchVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArticleService {
    public static boolean isContentValid(String input) {
        return input != null && input.matches("^(.{1,100000})$");
    }

    public static boolean isTitletValid(String input) {
        return input != null && input.matches("^(.{1,100})$");
    }

    private final ArticleMapper articleMapper;

    public Pair<ArticleVo[], PageVo> getBySearch(SearchVo searchVo, int page) {
        if (page < 1) {
            page = 1;
        }
        int totalCount = this.articleMapper.selectCountBySearch(searchVo);
        PageVo pageVo = new PageVo(10, page, totalCount);
        ArticleVo[] articles = this.articleMapper.selectBySearch(pageVo, searchVo);

        return Pair.of(articles, pageVo);
    }

    public ArticleEntity getById(int id) {
        if (id < 1) {
            return null;
        }
        return this.articleMapper.selectById(id);
    }

    public ArticleEntity getNext(int id) {
        if (id < 1) {
            return null;
        }
        return this.articleMapper.selectNext(id);
    }

    public ArticleEntity getPrevious(int id) {
        if (id < 1) {
            return null;
        }
        return this.articleMapper.selectPrevious(id);
    }

    public Result delete(UserEntity user, int id) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        ArticleEntity dbArticle = this.articleMapper.selectById(id);

        if (dbArticle == null || dbArticle.isDeleted()) {
            return CommonResult.FAILURE_ABSENT;
        }
        if (!dbArticle.getUserEmail().equals(user.getEmail()) && !user.isAdmin()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }
        dbArticle.setDeleted(true);

        return this.articleMapper.update(dbArticle) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result incrementView(ArticleEntity article) {
        if (article == null || article.getId() < 1) {
            return CommonResult.FAILURE;
        }
        article.setView(article.getView() + 1);
        return this.articleMapper.update(article) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result modify(UserEntity user, ArticleEntity article) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        if (article == null
                || article.getId() < 1
                || !ArticleService.isTitletValid(article.getTitle())
                || !ArticleService.isContentValid(article.getContent())) {
            return CommonResult.FAILURE;
        }

        ArticleEntity dbArticle = this.articleMapper.selectById(article.getId());

        if (dbArticle == null || dbArticle.isDeleted()) {
            return CommonResult.FAILURE_ABSENT;
        }

        if (!dbArticle.getUserEmail().equals(user.getEmail()) && !user.isAdmin()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        dbArticle.setTitle(article.getTitle());
        dbArticle.setContent(article.getContent());
        dbArticle.setModifiedAt(LocalDateTime.now());

        return this.articleMapper.update(dbArticle) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result write(UserEntity user, ArticleEntity article) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        if (article == null
                || !ArticleService.isTitletValid(article.getTitle())
                || !ArticleService.isContentValid(article.getContent())) {
            return CommonResult.FAILURE;
        }

        article.setUserEmail(user.getEmail());
        article.setView(0);
        article.setCreatedAt(LocalDateTime.now());
        article.setModifiedAt(null);
        article.setDeleted(false);

        return this.articleMapper.insert(article) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Result coverWrite(int id, ArticleCoverEntity cover) {
        if (id < 1) {
            return CommonResult.FAILURE;
        }
        if (cover == null || cover.getCoverUrl1() == null) {
            return CommonResult.FAILURE;
        }
        cover.setArticleId(id);
        cover.setCreatedAt(LocalDateTime.now());
        return this.articleMapper.insertCover(cover) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

//    public Result reviewWrite(UserEntity user, ReviewEntity review) {
//        if (user == null || user.isDeleted() || user.isSuspended()) {
//            return CommonResult.FAILURE_SESSION_EXPIRED;
//        }
//
//        if (review == null
//                || !ArticleService.isTitletValid(article.getTitle())
//                || !ArticleService.isContentValid(article.getContent())) {
//            return CommonResult.FAILURE;
//        }
//
//        article.setUserEmail(user.getEmail());
//        article.setView(0);
//        article.setCreatedAt(LocalDateTime.now());
//        article.setModifiedAt(null);
//        article.setDeleted(false);
//
//        return this.articleMapper.insert(article) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
//    }


}
