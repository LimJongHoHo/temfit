package kr.co.limbin.temfit.controllers;

import kr.co.limbin.temfit.entities.BrandEntity;
import kr.co.limbin.temfit.entities.ProductEntity;
import kr.co.limbin.temfit.entities.SkinEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.services.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

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

    @RequestMapping(value = "/pay", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getPay() {
        return "item/pay";
    }

//    @RequestMapping(value = "/pay", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public String postPay(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, ReviewEntity review) {
//        Result result = this.articleService.reviewWrite(signedUser, review);
//        JSONObject response = new JSONObject();
//        response.put("result", result.toStringLower());
//        if (result == CommonResult.SUCCESS) {
//            response.put("id", review.getId());
//        }
//        return response.toString();
//    }

    @RequestMapping(value = "/write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getWrite(Model model) {
        BrandEntity[] brands = this.itemService.getBrandALl();
        SkinEntity[] skins = this.itemService.getSkinALl();
        model.addAttribute("brands", brands);
        model.addAttribute("skins", skins);
        return "item/write";
    }
}
