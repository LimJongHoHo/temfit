package kr.co.limbin.temfit.controllers;

import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.services.ArticleService;
import kr.co.limbin.temfit.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping(value = "/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final ImageService imageService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex(@SessionAttribute(value = "signedUser") UserEntity signedUser, @RequestParam(value = "id", required = false) int id, Model model) {

        ArticleEntity article = this.articleService.getById(id);
        ReviewEntity review = this.articleService.getByReviewId(id);
        model.addAttribute("article", article);
        model.addAttribute("review", review);
        model.addAttribute("user", signedUser);
        model.addAttribute("allowed", article != null && signedUser != null && (article.getUserEmail().equals(signedUser.getEmail()) || signedUser.isAdmin()));
        if (article != null) {
            this.articleService.incrementView(article);
        }
        return "article/index";
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchIndex(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, ArticleEntity article, ArticleCoverEntity cover) {
        Result result = this.articleService.modify(signedUser, article);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());
        if (result == CommonResult.SUCCESS) {
            Result coverResult = this.articleService.coverModify(article.getId(), cover);
            if (coverResult == CommonResult.FAILURE) {
                response.put("coverResult", coverResult.toStringLower());
            }
        }
        return response.toString();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postIndex(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, ArticleEntity article, ArticleCoverEntity cover) {
        Result result = this.articleService.write(signedUser, article);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());
        if (result == CommonResult.SUCCESS) {
            response.put("id", article.getId());
            Result coverResult = this.articleService.coverWrite(article.getId(), cover);
            if (coverResult == CommonResult.FAILURE) {
                response.put("coverResult", coverResult.toStringLower());
            }
        }
        return response.toString();
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(@RequestParam(value = "id", required = false) int id) {
        ImageEntity image = this.imageService.getById(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + image.getName())
                .contentLength(image.getData().length)
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(image.getData());
    }

    @RequestMapping(value = "/image", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postImage(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, @RequestParam(value = "upload", required = false) MultipartFile multipartFile) throws IOException {
        ImageEntity image = ImageEntity.builder()
                .name(multipartFile.getOriginalFilename())
                .contentType(multipartFile.getContentType())
                .data(multipartFile.getBytes())
                .build();
        Result result = this.imageService.add(signedUser, image);
        JSONObject response = new JSONObject();
        if (result == CommonResult.SUCCESS) {
            response.put("url", "/article/image?id=" + image.getId());
        } else if (result == CommonResult.FAILURE_SESSION_EXPIRED) {
            JSONObject error = new JSONObject();
            error.put("message", "세션이 만료되었거나 게시글을 작성할 권한이 없습니다. 관리자에게 문의해 주세요.");
            error.put("error", error);
        } else {
            JSONObject error = new JSONObject();
            error.put("message", "알 수 없는 이유로 이미지를 업로드하지 못하였습니다. 잠시 후 다시 시도해 주세요.");
            response.put("error", error);
        }

        return response.toString();
    }

    @RequestMapping(value = "/modify", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getModify(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, @RequestParam(value = "id", required = false) int id, Model model) {
        ArticleEntity article = this.articleService.getById(id);
        ArticleCoverEntity cover = this.articleService.getByIdCover(id);

        if (article != null && (signedUser == null || !article.getUserEmail().equals(signedUser.getEmail()) && signedUser.isAdmin())) {
            article = null;
        }
        model.addAttribute("article", article);
        model.addAttribute("cover", cover);

        return "article/modify";
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getWrite() {
        return "article/write";
    }

    @RequestMapping(value = "/review", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getReview() {
        return "article/review";
    }
}
