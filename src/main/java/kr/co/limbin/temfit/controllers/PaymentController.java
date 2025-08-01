package kr.co.limbin.temfit.controllers;

import kr.co.limbin.temfit.entities.*;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.services.ItemService;
import kr.co.limbin.temfit.services.PaymentService;
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
@RequestMapping(value = "/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ItemService itemService;

    @RequestMapping(value = "/pay-complete", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getPayComplete(@RequestParam(value = "id", required = false) int id, Model model) {
        PaymentEntity payment = this.paymentService.getById(id);
        model.addAttribute("payment", payment);

        return "payment/pay-complete";
    }

    @RequestMapping(value = "/pay", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getPayment(@RequestParam(value = "cartId", required = false) int cartId, Model model) {
        CartDetailVo[] cartDetails = this.itemService.getByCartId(cartId);
        CartDetailVo cartDetail = this.itemService.getPriceByCartId(cartId);
        if (cartDetails.length != 0) {
            model.addAttribute("cartDetails", cartDetails);
        }
        model.addAttribute("cartDetail", cartDetail);
        return "payment/pay";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postPayment(@SessionAttribute(value = "signedUser", required = false) UserEntity signedUser, PaymentEntity payment) {
        Result result = this.paymentService.paymentWrite(signedUser, payment);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());
        if (result == CommonResult.SUCCESS) {
            response.put("id", payment.getId());
        }
        return response.toString();
    }

    @RequestMapping(value = "/pay-one", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getPayOne(@RequestParam(value = "productId", required = false) int productId, Model model) {
        ProductEntity product = this.itemService.getByProductId(productId);
        BrandEntity brand = this.itemService.getByBrandId(product.getBrandId());
        CartDetailVo[] cartDetails = this.itemService.getByCartId(productId);

        model.addAttribute("product", product);
        model.addAttribute("brand", brand);
        model.addAttribute("cartDetail", cartDetails);

        return "payment/pay-one";
    }

    @RequestMapping(value = "/pay-one", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deletePayOne(@RequestParam(value = "productId") int productId) {
        Result result = this.paymentService.deleteProductById(productId);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());
        return response.toString();
    }

    @RequestMapping(value = "/pay-one", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchPayOne(@RequestParam(value = "cartDetailId") int cartDetailId, @RequestParam(value = "calc") String calc) {
        Result result = this.itemService.updateCartDetail(cartDetailId, calc);
        JSONObject response = new JSONObject();
        response.put("result", result.toStringLower());
        return response.toString();
    }
}
