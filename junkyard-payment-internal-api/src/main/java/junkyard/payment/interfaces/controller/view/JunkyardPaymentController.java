package junkyard.payment.interfaces.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/junkyard")
public class JunkyardPaymentController {

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/fail")
    public String failed() {
        return "fail";
    }
}
