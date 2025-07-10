package kr.co.limbin.temfit.controllers;

import kr.co.limbin.temfit.entities.ReviewEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.results.CommonResult;
import kr.co.limbin.temfit.results.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequestMapping(value = "/item")
@RequiredArgsConstructor
public class ItemController {

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
    public String getWrite() {
        return "item/write";
    }
}
