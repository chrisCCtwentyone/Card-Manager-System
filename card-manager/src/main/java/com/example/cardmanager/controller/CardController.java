package com.example.cardmanager.controller;

import jakarta.validation.Valid;
import com.example.cardmanager.entity.Card;
import com.example.cardmanager.entity.User;
import com.example.cardmanager.dto.CardDto;
import com.example.cardmanager.dto.TransactionDto;
import com.example.cardmanager.service.CardService;
import com.example.cardmanager.service.TransactionService;
import com.example.cardmanager.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Iterator;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CardController {

    private CardService cardService;
    private TransactionService transactionService;
    private UserService userService;

    public CardController(CardService cardService, TransactionService transactionService, UserService userService) {
        this.cardService = cardService;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping("/addcard")

    public String showCardForm(Model model) {
        CardDto card = new CardDto();
        model.addAttribute("card", card);
        return "addcard";
    }

    @PostMapping("/addcard/save")
    public String addCard(@Valid @ModelAttribute("card") CardDto cardDto,
            BindingResult result,
            Model model) {
        Card existingCard = cardService.findCardByCnumber(cardDto.getCnumber());
        User existingIntestatario = userService.findUserByEmail(cardDto.getEmail_intestatario());

        if (existingIntestatario == null) {
            result.rejectValue("email_intestatario", null,
                    "Non esistono utenti con questa mail registrata");
            return "/addcard";
        } else {

            if (existingCard != null && existingCard.getCnumber() != null && !existingCard.getCnumber().isEmpty()) {
                result.rejectValue("cnumber", null,
                        "Esiste già una carta con questo numero");
            }

            if (result.hasErrors()) {
                model.addAttribute("card", cardDto);
                return "/addcard";
            }

            cardService.saveCard(cardDto, existingIntestatario);
            return "redirect:/addcard?success";
        }
    }

    @GetMapping("/cards")
    public String users(Model model) {
        List<CardDto> cards = cardService.findAllCards();
        model.addAttribute("cards", cards);
        return "cards";
    }

    @GetMapping("/searchcard")
    public String searchCard(Model model) {
        return "searchcard";
    }

    @PostMapping("/searchcard")
    public String searchCardResult(String cnumber,
            Model model) {
        Card existingCard = cardService.findCardByCnumber(cnumber);

        if (existingCard == null || existingCard.getCnumber() == null || existingCard.getCnumber().isEmpty()) {
            model.addAttribute("notfoundcarta", true);
            return "searchcard";
        }

        else {
            model.addAttribute("cnumber", existingCard.getCnumber());
            model.addAttribute("saldo", existingCard.getSaldo());
            model.addAttribute("email_intestatario", existingCard.getEmail_proprietario());
            model.addAttribute("stato", existingCard.getStato());
            model.addAttribute("success", true);
        }
        return "searchcard";

    }

    @GetMapping("/blockcard")
    public String showBlockCardForm(Model model) {
        return "blockcard";
    }

    @RequestMapping(value = "/blockcard", method = RequestMethod.POST, params = "block")
    public String blockCard(String cnumber, Model model) {
        Card existingCard = cardService.findCardByCnumber(cnumber);

        if (existingCard == null || existingCard.getCnumber() == null || existingCard.getCnumber().isEmpty()) {
            model.addAttribute("notACard", true);
            return "blockcard";
        }

        else {
            cardService.blockCard(cnumber);
            model.addAttribute("cnumber", existingCard.getCnumber());
            model.addAttribute("saldo", existingCard.getSaldo());
            model.addAttribute("email_intestatario", existingCard.getEmail_proprietario());
            model.addAttribute("stato", existingCard.getStato());
            model.addAttribute("success", true);
            showBlockCardForm(model);
        }
        return "blockcard";
    }

    @RequestMapping(value = "/blockcard", method = RequestMethod.POST, params = "attiva")
    public String attivaCard(String cnumber, Model model) {
        Card existingCard = cardService.findCardByCnumber(cnumber);

        if (existingCard == null || existingCard.getCnumber() == null || existingCard.getCnumber().isEmpty()) {
            model.addAttribute("notACard", true);

            return "blockcard";
        }

        else {
            cardService.activeCard(cnumber);
            model.addAttribute("cnumber", existingCard.getCnumber());
            model.addAttribute("saldo", existingCard.getSaldo());
            model.addAttribute("email_intestatario", existingCard.getEmail_proprietario());
            model.addAttribute("stato", existingCard.getStato());
            model.addAttribute("success", true);
            showBlockCardForm(model);
        }
        return "blockcard";
    }

    @GetMapping("/operation")
    public String operationCard(Model model) {
        return "operation";
    }

    @RequestMapping(value = "/operation", method = RequestMethod.POST, params = "recharge")
    public String doRecharge(String cnumber, float amount, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Card existingCard = cardService.findCardByCnumber(cnumber);
        User actualSeller = userService.findUserByEmail(currentPrincipalName);
        if (actualSeller.getStatoutente().equals("Bloccato")) {
            model.addAttribute("sellerBlocked", true);
            return "operation";
        } else {

            if (existingCard == null || existingCard.getCnumber() == null || existingCard.getCnumber().isEmpty()) {
                model.addAttribute("errorNotFound", true);
            }

            else {
                if (existingCard.getStato().equals("Bloccata")) {
                    model.addAttribute("failure", true);
                } else {
                    if (existingCard.getSaldo() + amount > 9000) {
                        model.addAttribute("troppisoldi", true);
                        return "operation";
                    } else {
                        model.addAttribute("success", true);
                        cardService.rechargeCard(cnumber, amount);
                        transactionService.saveTransaction(cnumber, amount, "Ricarica", currentPrincipalName);
                        model.addAttribute("cnumber", existingCard.getCnumber());
                        model.addAttribute("saldo", existingCard.getSaldo());
                        model.addAttribute("email_intestatario", existingCard.getEmail_proprietario());
                        model.addAttribute("stato", existingCard.getStato());
                    }
                }
            }
        }
        return "operation";
    }

    @RequestMapping(value = "/operation", method = RequestMethod.POST, params = "payment")
    public String doPayment(String cnumber, float amount, Model model) {
        Card existingCard = cardService.findCardByCnumber(cnumber);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User actualSeller = userService.findUserByEmail(currentPrincipalName);
        if (actualSeller.getStatoutente().equals("Bloccato")) {
            model.addAttribute("sellerBlocked", true);
            return "operation";
        } else {

            if (existingCard == null || existingCard.getCnumber() == null || existingCard.getCnumber().isEmpty()) {
                model.addAttribute("errorNotFound", true);
            }

            else {
                if (existingCard.getStato().equals("Bloccata")) {
                    model.addAttribute("failure", true);
                } else {
                    if (existingCard.getSaldo() - amount < 0) {
                        model.addAttribute("saldoinsufficente", true);
                        return "operation";
                    } else {
                        cardService.payCard(cnumber, amount);
                        transactionService.saveTransaction(cnumber, amount, "Pagamento", currentPrincipalName);
                        model.addAttribute("cnumber", existingCard.getCnumber());
                        model.addAttribute("saldo", existingCard.getSaldo());
                        model.addAttribute("email_intestatario", existingCard.getEmail_proprietario());
                        model.addAttribute("stato", existingCard.getStato());
                        model.addAttribute("success", true);
                    }
                }
            }
        }
        return "operation";

    }

    @GetMapping("/transactions")
    public String transactions(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<TransactionDto> transactions = transactionService.findAllTransactions();
        Iterator<TransactionDto> iterator = transactions.iterator();
        if (currentPrincipalName.equals("admin@cardmanager.it")) {
            model.addAttribute("transactions", transactions);
            return "transactions";
        } else {

            while (iterator.hasNext()) {
                TransactionDto transaction = iterator.next();
                Card existingCard = cardService.findCardByCnumber(transaction.getNumerocarta());
                if (!existingCard.getEmail_proprietario().equals(currentPrincipalName)) {

                    iterator.remove();
                }
            }
        }
        model.addAttribute("transactions", transactions);
        return "transactions";
    }
}
