package kr.co.limbin.temfit.controllers;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.services.ArticleService;
import kr.co.limbin.temfit.services.ItemService;
import kr.co.limbin.temfit.services.PaymentService;
import kr.co.limbin.temfit.vos.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getMain() {
        return "redirect:/main";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, HttpServletRequest request, Model model) {
        if (signedUser == null) {
            System.out.println("로그인 안 됨");
        } else {
            System.out.printf("로그인 됨 (%s, %s, %s)\n",
                    signedUser.getNickname(),
                    request.getRemoteAddr(),
                    request.getRequestURI());
        }
        ProductVo[] products = this.itemService.getProductAll();
        SkinVo[] skins = this.itemService.getSkinALl();
        BrandVo[] brands = this.itemService.getBrandALl();

        model.addAttribute("products", products);
        model.addAttribute("skins", skins);
        model.addAttribute("brands", brands);

        return "main/index";
    }

    @RequestMapping(value = "/skinMainId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postSkinMainId(@RequestParam(value = "skinId") int skinId) {

        JSONObject response = new JSONObject();
        response.put("products", this.itemService.getProductBySkinId(skinId));
        return response.toString();
    }

    @RequestMapping(value = "/productId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postProductId(@RequestParam(value = "productId") int productId) {
        ArticleEntity article = this.itemService.getArticleIdByProductId(productId);
        JSONObject response = new JSONObject();
        response.put("articleId",article.getId());
        return response.toString();
    }

    @RequestMapping(value = "/rank", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getRank(Model model) {

        SkinEntity[] skins = this.itemService.getSkinALl();
        BrandEntity[] brands = this.itemService.getBrandALl();
        model.addAttribute("skins", skins);
        model.addAttribute("brands", brands);

        return "main/rank";
    }

    @RequestMapping(value = "/rankBrand", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getRankBrand(Model model) {

        SkinEntity[] skins = this.itemService.getSkinALl();
        BrandEntity[] brands = this.itemService.getBrandALl();
        model.addAttribute("brands", brands);
        model.addAttribute("skins", skins);

        return "main/rankBrand";
    }

    @RequestMapping(value = "/brandId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postBrandId(@RequestParam(value = "brandId") int brandId) {

        JSONObject response = new JSONObject();
        response.put("products", this.itemService.getProductByBrandId(brandId));

        return response.toString();
    }

    @RequestMapping(value = "/skinId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postSkinId(@RequestParam(value = "skinId") int skinId) {

        JSONObject response = new JSONObject();
        response.put("products", this.itemService.getProductBySkinId(skinId));

        return response.toString();
    }

    @RequestMapping(value = "/index_search", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndexSearch(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword, Model model) {
        ProductVo[] products = this.articleService.search(keyword);
        String aaa = "\"";
        if (products.length == 0) {
            model.addAttribute("products", null);
        } else {
            model.addAttribute("products", products);
        }
        model.addAttribute("aaa", aaa);
        model.addAttribute("keyword", keyword);
        System.out.println(keyword);

        return "main/index_search";
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getCart(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, Model model) {
        Integer id = this.itemService.getCartId(signedUser.getEmail());

        if (id == null) {
            if (this.itemService.insertCart(signedUser) == null) {
                // 문제 생겼을때 갈만한 페이지....
            }
            id = this.itemService.getCartId(signedUser.getEmail());
        }

        CartDetailVo[] cartDetails = this.itemService.getByCartId(id);
        model.addAttribute("cartId", id);
        if (cartDetails.length != 0) {
            model.addAttribute("cartDetails", cartDetails);
        }
        return "main/cart";
    }
}
