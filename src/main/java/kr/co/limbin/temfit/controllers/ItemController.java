package kr.co.limbin.temfit.controllers;

import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.services.ItemService;
import kr.co.limbin.temfit.vos.CartDetailVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping(value = "/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, ProductEntity product) {
        Result result = this.itemService.insert(signedUser, product);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());
        return response.toString();
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getWrite(Model model) {
        BrandEntity[] brands = this.itemService.getBrandALl();
        SkinEntity[] skins = this.itemService.getSkinALl();
        model.addAttribute("brands", brands);
        model.addAttribute("skins", skins);
        return "item/write";
    }

    @RequestMapping(value = "/cart-detail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postCart(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, CartDetailEntity cartDetail) {
        Integer cartId = this.itemService.getCartId(signedUser.getEmail());
        if (cartId == null) {
            if (this.itemService.insertCart(signedUser) == null) {
                // 문제 생겼을때 갈만한 페이지....
            }
            cartId = this.itemService.getCartId(signedUser.getEmail());
        }
        cartDetail.setCartId(cartId);
        cartDetail.setCreatUserEmail(signedUser.getEmail());
        Result result = this.itemService.insertCartDetail(cartDetail);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());

        return response.toString();
    }

    @RequestMapping(value = "/cart-detail", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteCartDetail(@RequestParam(value = "cartDetailId") int cartDetailId) {
        Result result = this.itemService.deleteCartDetail(cartDetailId);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());
        return response.toString();
    }

    @RequestMapping(value = "/cart-detail", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchCartDetail(@RequestParam(value = "cartDetailId") int cartDetailId, @RequestParam(value = "calc") String calc) {
        Result result = this.itemService.updateCartDetail(cartDetailId, calc);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());
        return response.toString();
    }

}
