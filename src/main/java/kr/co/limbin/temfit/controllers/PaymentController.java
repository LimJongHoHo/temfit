package kr.co.limbin.temfit.controllers;

import kr.co.limbin.temfit.entities.PaymentEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import kr.co.limbin.temfit.services.PaymentService;
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

    @RequestMapping(value = "/pay-complete", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getPayComplete(@RequestParam(value = "id", required = false) int id, Model model) {
        PaymentEntity payment = this.paymentService.getById(id);
        model.addAttribute("payment", payment);

        return "payment/pay-complete";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getPayment() {
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
}
