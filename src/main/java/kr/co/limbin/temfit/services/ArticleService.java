package kr.co.limbin.temfit.services;

import kr.co.limbin.temfit.entities.ArticleCoverEntity;
import kr.co.limbin.temfit.entities.ArticleEntity;
import kr.co.limbin.temfit.entities.ReviewEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.mappers.ArticleMapper;
import kr.co.limbin.temfit.mappers.ReviewMapper;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.results.ResultTuple;
import kr.co.limbin.temfit.vos.ArticleVo;
import kr.co.limbin.temfit.vos.PageVo;
import kr.co.limbin.temfit.vos.ReviewVo;
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

    public static boolean isTitleValid(String input) {
        return input != null && input.matches("^(.{1,100})$");
    }

    private final ArticleMapper articleMapper;
    private final ReviewMapper reviewMapper;

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

    public ReviewVo[] getByReviewAll(int articleId) {
        if (articleId < 1) {
            return null;
        }
        return this.reviewMapper.selectByAll(articleId);
    }

    public ReviewVo[] getTwoReview(int articleId) {
        if (articleId < 1) {
            return null;
        }
        return this.reviewMapper.selectTwoReview(articleId);
    }

    public ReviewVo getByReviewId(int id) {
        if (id < 1) {
            return null;
        }
        return this.reviewMapper.selectByReviewId(id);
    }

    public int getTotalCount(int articleId) {
        return this.reviewMapper.countReview(articleId);
    }


    public ArticleCoverEntity getByIdCover(int id) {
        if (id < 1) {
            return null;
        }
        return this.articleMapper.selectByIdCover(id);
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
                || !ArticleService.isTitleValid(article.getTitle())
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
                || !ArticleService.isTitleValid(article.getTitle())
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

    public Result coverModify(int id, ArticleCoverEntity cover) {
        if (id < 1) {
            return CommonResult.FAILURE;
        }
        if (cover == null || cover.getCoverUrl1() == null) {
            return CommonResult.FAILURE;
        }
        ArticleCoverEntity dbCover = this.articleMapper.selectByIdCover(id);

        if (dbCover == null || dbCover.getArticleId() < 1 || dbCover.getCoverUrl1() == null) {
            return CommonResult.FAILURE;

        }
        dbCover.setCoverUrl1(cover.getCoverUrl1());
        dbCover.setCoverUrl2(cover.getCoverUrl2());
        dbCover.setCoverUrl3(cover.getCoverUrl3());
        dbCover.setCoverUrl4(cover.getCoverUrl4());
        dbCover.setCoverUrl5(cover.getCoverUrl5());
        dbCover.setCoverUrl6(cover.getCoverUrl6());
        dbCover.setCoverUrl7(cover.getCoverUrl7());
        dbCover.setCoverUrl8(cover.getCoverUrl8());
        dbCover.setCreatedAt(LocalDateTime.now());
        return this.articleMapper.updateCover(dbCover) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
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

    public Result reviewWrite(UserEntity user, ReviewEntity review) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        if (review == null
                || !ArticleService.isContentValid(review.getContent())) {
            return CommonResult.FAILURE;
        }

        review.setArticleId(1); //하드코딩
        review.setUserEmail(user.getEmail());
        review.setCreatedAt(LocalDateTime.now());
        review.setModifiedAt(null);
        review.setDeleted(false);

        return this.reviewMapper.insert(review) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public ResultTuple<ReviewVo> reviewModify(UserEntity user, ReviewVo review) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return ResultTuple.<ReviewVo>builder().result(CommonResult.FAILURE_SESSION_EXPIRED).build();
        }

        if (review == null
                || review.getId() < 1
                || !ArticleService.isContentValid(review.getContent())) {
            return ResultTuple.<ReviewVo>builder().result(CommonResult.FAILURE).build();
        }

        ReviewVo dbReview = this.reviewMapper.selectByReviewId(review.getId());

        if (dbReview == null || dbReview.isDeleted()) {
            return ResultTuple.<ReviewVo>builder().result(CommonResult.FAILURE_ABSENT).build();
        }

        if (!dbReview.getUserEmail().equals(user.getEmail()) && !user.isAdmin()) {
            return ResultTuple.<ReviewVo>builder().result(CommonResult.FAILURE_SESSION_EXPIRED).build();
        }

        dbReview.setContent(review.getContent());
        dbReview.setModifiedAt(LocalDateTime.now());

        return this.reviewMapper.update(dbReview) > 0 ? ResultTuple.<ReviewVo>builder().result(CommonResult.SUCCESS).payload(dbReview).build() : ResultTuple.<ReviewVo>builder().result(CommonResult.FAILURE).build();
    }

    public Result reviewDelete(UserEntity user, int id) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        ReviewVo dbReview = this.reviewMapper.selectByReviewId(id);

        if (dbReview == null || dbReview.isDeleted()) {
            return CommonResult.FAILURE_ABSENT;
        }
        if (!dbReview.getUserEmail().equals(user.getEmail()) && !user.isAdmin()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        dbReview.setDeleted(true);

        return this.reviewMapper.update(dbReview) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

}
