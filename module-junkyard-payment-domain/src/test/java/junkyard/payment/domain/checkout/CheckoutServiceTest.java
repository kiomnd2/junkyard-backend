package junkyard.payment.domain.checkout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = CheckoutServiceImpl.class)
class CheckoutServiceTest {

    @Autowired
    private CheckoutService checkoutService;

    @Test
    void savePaymentAndPaymentOrder_success() {

    }

}