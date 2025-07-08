package kr.co.limbin.temfit.controllers;

import kr.co.limbin.temfit.entities.ReviewEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.results.ResultTuple;
import kr.co.limbin.temfit.services.ArticleService;
import kr.co.limbin.temfit.vos.ReviewVo;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ArticleService articleService;

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getList(@RequestParam(value = "articleId", required = false) int articleId, Model model) {

        ReviewVo[] reviews = this.articleService.getByReviewAll(articleId);
        model.addAttribute("reviews", reviews);

        return "review/list";
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getReview() {
        return "review/write";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postReview(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, ReviewEntity review) {
        Result result = this.articleService.reviewWrite(signedUser, review);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());
        if (result == CommonResult.SUCCESS) {
            response.put("id", review.getId());
        }
        return response.toString();
    }

    @RequestMapping(value = "/modify", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getModify(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, @RequestParam(value = "id", required = false) int id, Model model) {
        ReviewVo review = this.articleService.getByReviewId(id);

        model.addAttribute("review", review);

        return "review/modify";
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchModify(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, ReviewVo review) {
        ResultTuple<ReviewVo> result = this.articleService.reviewModify(signedUser, review);
        JSONObject response = new JSONObject();
        response.put("result", result.getResult().toStringLower());
        if (result.getResult() == CommonResult.SUCCESS) {
            response.put("articleId", result.getPayload().getArticleId());
        }
        return response.toString();
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteList(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, @RequestParam(value = "id", required = false) int id) {
        Result result = this.articleService.reviewDelete(signedUser, id);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());

        return response.toString();
    }
}
