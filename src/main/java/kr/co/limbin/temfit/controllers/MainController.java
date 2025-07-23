package kr.co.limbin.temfit.controllers;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.services.ArticleService;
import kr.co.limbin.temfit.services.ItemService;
import kr.co.limbin.temfit.services.PaymentService;
import kr.co.limbin.temfit.vos.*;
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
    private final ItemService itemService;
    private final ArticleService articleService;
    private final PaymentService paymentService;

    @RequestMapping(value = "/main", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser,  HttpServletRequest request, Model model) {
        if (signedUser == null) {
            System.out.println("로그인 안 됨");
        } else {
            System.out.printf("로그인 됨 (%s, %s, %s)\n",
                    signedUser.getNickname(),
                    request.getRemoteAddr(),
                    request.getRequestURI());
        }
        ProductVo[] products = this.itemService.getProductAll();
        ProductVo[] skins = this.itemService.getProductBySkinAll();
        ProductVo[] brands = this.itemService.getProductByBrandAll();
        model.addAttribute("products", products);
        model.addAttribute("skins", skins);
        model.addAttribute("brands", brands);

        return "main/index";
    }

    @RequestMapping(value = "/rank", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getRank(Model model) {

        BrandEntity[] brands = this.itemService.getBrandALl();
        SkinEntity[] skins = this.itemService.getSkinALl();

        model.addAttribute("brands", brands);
        model.addAttribute("skins", skins);

        return "main/rank";
    }

    @RequestMapping(value = "/index_search", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndexSearch(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword, Model model) {
        ProductVo[] products = this.articleService.search(keyword);
        if (products.length == 0) {
            model.addAttribute("products", null);
        } else {
            model.addAttribute("products", products);
        }
        model.addAttribute("keyword", "\"" + keyword + "\"");

        return "main/index_search";
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getCart(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, Model model) {
        Integer id = this.itemService.getCartId("asdf1234@naver.com");

        CartDetailVo[] cartDetails = this.itemService.getByCartId(id);
        model.addAttribute("cartId", id);
        if (cartDetails.length != 0) {
            model.addAttribute("cartDetails", cartDetails);
        }
        return "main/cart";
    }
}
