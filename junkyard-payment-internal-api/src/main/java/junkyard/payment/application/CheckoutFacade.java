package junkyard.payment.application;

import junkyard.payment.domain.checkout.CheckoutResult;
import junkyard.payment.domain.checkout.CheckoutService;
import junkyard.payment.interfaces.controller.api.CheckoutDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CheckoutFacade {
    private final CheckoutService checkoutService;

    public CheckoutResult checkout(Long authId, CheckoutDto.CheckoutRequest request) {
        return checkoutService.checkout(authId, request.toCommand());
    }
}
