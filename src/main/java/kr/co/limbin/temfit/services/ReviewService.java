package kr.co.limbin.temfit.services;

import kr.co.limbin.temfit.entities.ReviewEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.mappers.ReviewMapper;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.results.ResultTuple;
import kr.co.limbin.temfit.vos.ReviewVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;

    public ReviewVo[] getByReviewAll(int articleId) {
        if (articleId < 1) {
            return null;
        }
        return this.reviewMapper.selectByAll(articleId);
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

    public Result reviewWrite(UserEntity user, ReviewEntity review) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }

        if (review == null
                || !ArticleService.isContentValid(review.getContent())) {
            return CommonResult.FAILURE;
        }

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
