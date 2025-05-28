package com.olga.testForPaytech.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olga.testForPaytech.model.Payment;
import com.olga.testForPaytech.service.PaymentAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.olga.testForPaytech.constants.Constants.*;

@Controller
public class PaymentController {

    @GetMapping(ADD_PAYMENT)
    public String showForm(Model model) {
        model.addAttribute(PAYMENT, new Payment());
        return ADD_PAYMENT_NAME_OF_FILE;
    }

    @PostMapping(ADD_PAYMENT)
    public String submitForm(@ModelAttribute Payment payment, RedirectAttributes redirectAttributes) {
        double amount = payment.getAmount();

        PaymentAPIService paymentAPIService = new PaymentAPIService();

        try {
            ResponseEntity<String> response = paymentAPIService.sentPostRequest(amount);
            String url = paymentAPIService.parseAnswer(response.getBody());

            return REDIRECT + url;
        } catch (HttpClientErrorException httpClientErrorException) {
            String error = httpClientErrorException.getStatusCode().toString();

            String[] errors = error.split(" ");

            redirectAttributes.addFlashAttribute(STATUS_CODE, errors[0]);
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, errors[1]);
        } catch (JsonProcessingException e) {
            return REDIRECT + ERROR_PAGE_NAME_OF_FILE;
        }

        return REDIRECT + ERROR_PAYMENT_NAME_OF_FILE;
    }

    @GetMapping(ERROR_PAYMENT)
    public String showErrorForm(Model model) {
        return ERROR_PAYMENT_NAME_OF_FILE;
    }

    @GetMapping(ERROR_PAGE)
    public String showErrorPageForm(Model model) {
        return ERROR_PAGE_NAME_OF_FILE;
    }
}
