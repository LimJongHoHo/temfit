package kr.co.limbin.temfit.controllers;

import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.services.ItemService;
import kr.co.limbin.temfit.vos.ReviewVo;
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

    @RequestMapping(value = "/pay-complete", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getPayComplete(@RequestParam(value = "paymentId", required = false) int paymentId, Model model) {
        PaymentEntity payment = this.itemService.getByPaymentAll(paymentId);
        model.addAttribute("payment", payment);

        return "item/pay-complete";
    }

    @RequestMapping(value = "/pay", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getPay() {
        return "item/pay";
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postPay(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, PaymentEntity payment) {
        Result result = this.itemService.paymentWrite(signedUser, payment);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());
        if (result == CommonResult.SUCCESS) {
            response.put("id", payment.getId());
        }
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
}
