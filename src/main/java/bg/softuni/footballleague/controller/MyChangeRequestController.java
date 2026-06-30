package bg.softuni.footballleague.controller;

import bg.softuni.footballleague.service.ChangeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my-proposals")
public class MyChangeRequestController {

    private final ChangeRequestService changeRequestService;

    @GetMapping
    public String myProposals(Authentication authentication, Model model) {
        model.addAttribute("changeRequests", changeRequestService.findMine(authentication));
        return "my-proposals";
    }
}
