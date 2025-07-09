package kr.co.limbin.temfit.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequestMapping(value = "/")
@RequiredArgsConstructor
public class ItemController {

    @RequestMapping(value = "/pay", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getPay() {
        return "item/pay";
    }

}
