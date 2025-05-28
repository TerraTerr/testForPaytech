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

@Controller
public class PaymentController {

    @GetMapping("/add-payment")
    public String showForm(Model model) {
        model.addAttribute("payment", new Payment());
        return "add-payment";
    }

    @PostMapping("/add-payment")
    public String submitForm(@ModelAttribute Payment payment, RedirectAttributes redirectAttributes) {
        double amount = payment.getAmount();

        PaymentAPIService paymentAPIService = new PaymentAPIService();

        try {
            ResponseEntity<String> response = paymentAPIService.sentPostRequest(amount);
            String url = paymentAPIService.parseAnswer(response.getBody());
            return "redirect:" + url;
        } catch (HttpClientErrorException httpClientErrorException) {
            String error = httpClientErrorException.getStatusCode().toString();

            String[] errors = error.split(" ");

            redirectAttributes.addFlashAttribute("statusCode", Integer.parseInt(errors[0]));
            redirectAttributes.addFlashAttribute("errorMessage", errors[1]);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/error-payment";
    }

    @GetMapping("/error-payment")
    public String showErrorForm(Model model) {
        return "error-payment";
    }
}
