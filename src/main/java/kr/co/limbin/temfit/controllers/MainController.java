package kr.co.limbin.temfit.controllers;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.limbin.temfit.entities.ReviewEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.services.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping(value = "/")
@RequiredArgsConstructor
public class MainController {

    private final ArticleService articleService;

    @RequestMapping(value = "/main", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex(@SessionAttribute(value = "signedUser", required = false)  UserEntity signedUser ,HttpServletRequest request) {
        if (signedUser == null) {
            System.out.println("로그인 안 됨");
        } else {
            System.out.printf("로그인 됨 (%s, %s, %s)\n",
                    signedUser.getNickname(),
                    request.getRemoteAddr(),
                    request.getRequestURI());
        }
        return "main/index";
    }

    @RequestMapping(value = "/rank", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getRank() {
        return "main/rank";
    }

    @RequestMapping(value = "/index_search", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndexSearch() {
        return "main/index_search";
    }

    @RequestMapping(value = "/brand", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getBrand() {
        return "main/brand";
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getCart() {
        return "main/cart";
    }

    @RequestMapping(value = "/review", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getReview(@SessionAttribute(value = "signedUser") UserEntity signedUser, @RequestParam(value = "id", required = false) int id, Model model){
        ReviewEntity review = this.articleService.getByReviewId(id);
        model.addAttribute("review", review);
        model.addAttribute("allowed", review != null && signedUser != null && (review.getUserEmail().equals(signedUser.getEmail()) || signedUser.isAdmin()));

        return "main/index";
    }

}
