package kr.co.limbin.temfit.controllers;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.limbin.temfit.entities.BrandEntity;
import kr.co.limbin.temfit.entities.ProductEntity;
import kr.co.limbin.temfit.entities.SkinEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.services.ItemService;
import kr.co.limbin.temfit.vos.CartDetailVo;
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

    @RequestMapping(value = "/main", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, HttpServletRequest request) {
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
    public String getRank(Model model) {

        BrandEntity[] brands = this.itemService.getBrandALl();
        SkinEntity[] skins = this.itemService.getSkinALl();
        ProductEntity[] products = this.itemService.getProductAll();
        model.addAttribute("brands", brands);
        model.addAttribute("skins", skins);
        model.addAttribute("products", products);

        return "main/rank";
    }

    @RequestMapping(value = "/index_search", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndexSearch() {
        return "main/index_search";
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getCart(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, Model model) {
        Integer id = this.itemService.getCartId(signedUser.getEmail());

        CartDetailVo[] cartDetails = this.itemService.getByCartId(id);

        if (cartDetails.length != 0) {
            model.addAttribute("cartDetails", cartDetails);
        }
        return "main/cart";
    }
}
