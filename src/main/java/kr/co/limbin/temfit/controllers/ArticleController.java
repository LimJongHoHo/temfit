package kr.co.limbin.temfit.controllers;

import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.services.ArticleService;
import kr.co.limbin.temfit.services.ImageService;
import kr.co.limbin.temfit.services.ItemService;
import kr.co.limbin.temfit.vos.ReviewVo;
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
import java.util.Base64;

@Controller
@RequestMapping(value = "/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final ImageService imageService;
    private final ItemService itemService;

    @RequestMapping(value = "/test", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postTest(@RequestParam(value = "src") String src) {
        String encode = Base64.getEncoder().encodeToString(src.getBytes());
        String decode =  new String(Base64.getDecoder().decode(encode));
        return null;
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteIndex(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, @RequestParam(value = "id", required = false) int id, @RequestParam(value = "productId", required = false) int productId) {
        Result result = this.articleService.delete(signedUser, id);
        Result result2 = this.itemService.delete(signedUser, productId);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());
        response.put("result2", result2.toStringLower());

        return response.toString();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex(@RequestParam(value = "id", required = false) int id, Model model) {
        ArticleEntity article = this.articleService.getById(id);
        ReviewVo[] reviews = this.articleService.getTwoReview(id);
        ArticleCoverEntity cover = this.articleService.getByIdCover(id);
        int totalCount = this.articleService.getTotalCount(id);
        model.addAttribute("article", article);
        model.addAttribute("reviews", reviews);
        model.addAttribute("cover", cover);
        model.addAttribute("totalCount", totalCount);
        if (article != null) {
            this.articleService.incrementView(article);
            ProductEntity product = this.itemService.getByProductId(article.getProductId());
            BrandEntity brand = this.itemService.getByBrandId(product.getBrandId());
            model.addAttribute("product", product);
            model.addAttribute("brand", brand);
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
        ProductEntity[] products = this.itemService.getProductAll();
        if (article != null && (signedUser == null || !article.getUserEmail().equals(signedUser.getEmail()) && signedUser.isAdmin())) {
            article = null;
        }
        model.addAttribute("article", article);
        model.addAttribute("cover", cover);
        model.addAttribute("products", products);

        return "article/modify";
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getWrite(Model model) {
        ProductEntity[] products = this.itemService.getProductAll();
        model.addAttribute("products", products);
        return "article/write";
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postCheck(@RequestParam(value = "productId") int productId) {
        ArticleEntity article = this.itemService.getArticleIdByProductId(productId);
        JSONObject response = new JSONObject();
        if (article == null) {
            response.put("articleId", 0);
        } else {
            response.put("articleId", article.getId());
        }

        return response.toString();
    }
}