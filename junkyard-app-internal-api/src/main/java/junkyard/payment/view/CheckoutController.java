package junkyard.payment.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/payment")
public class CheckoutController {

    @GetMapping("/")
    public String checkoutPage() {
        return "checkout";
    }
}
