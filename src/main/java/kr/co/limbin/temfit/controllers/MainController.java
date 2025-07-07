package kr.co.limbin.temfit.controllers;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.services.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping(value = "/")
@RequiredArgsConstructor
public class MainController {
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
}
