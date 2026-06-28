package bg.softuni.footballleague.controller;

import bg.softuni.footballleague.dto.MatchDto;
import bg.softuni.footballleague.service.MatchService;
import bg.softuni.footballleague.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;
    private final TeamService teamService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("matches", matchService.findAll());
        return "matches/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("matchDto", new MatchDto());
        model.addAttribute("teams", teamService.findAll());
        return "matches/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("matchDto") MatchDto matchDto, BindingResult bindingResult,
                          Model model) {
        if (matchDto.getHomeTeamId() != null && matchDto.getHomeTeamId().equals(matchDto.getAwayTeamId())) {
            bindingResult.rejectValue("awayTeamId", "team.same", "Away team must differ from home team");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("teams", teamService.findAll());
            return "matches/form";
        }

        matchService.create(matchDto);
        return "redirect:/matches";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable UUID id, Model model) {
        model.addAttribute("matchDto", matchService.findById(id));
        model.addAttribute("teams", teamService.findAll());
        return "matches/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable UUID id, @Valid @ModelAttribute("matchDto") MatchDto matchDto,
                        BindingResult bindingResult, Model model) {
        if (matchDto.getHomeTeamId() != null && matchDto.getHomeTeamId().equals(matchDto.getAwayTeamId())) {
            bindingResult.rejectValue("awayTeamId", "team.same", "Away team must differ from home team");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("teams", teamService.findAll());
            return "matches/form";
        }

        matchService.update(id, matchDto);
        return "redirect:/matches";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id) {
        matchService.delete(id);
        return "redirect:/matches";
    }
}
