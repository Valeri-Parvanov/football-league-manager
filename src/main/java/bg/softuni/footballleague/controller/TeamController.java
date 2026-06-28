package bg.softuni.footballleague.controller;

import bg.softuni.footballleague.dto.TeamDto;
import bg.softuni.footballleague.service.LeagueService;
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
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final LeagueService leagueService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("teams", teamService.findAll());
        return "teams/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("teamDto", new TeamDto());
        model.addAttribute("leagues", leagueService.findAll());
        return "teams/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("teamDto") TeamDto teamDto, BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("leagues", leagueService.findAll());
            return "teams/form";
        }

        teamService.create(teamDto);
        return "redirect:/teams";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable UUID id, Model model) {
        model.addAttribute("teamDto", teamService.findById(id));
        model.addAttribute("leagues", leagueService.findAll());
        return "teams/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable UUID id, @Valid @ModelAttribute("teamDto") TeamDto teamDto,
                        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("leagues", leagueService.findAll());
            return "teams/form";
        }

        teamService.update(id, teamDto);
        return "redirect:/teams";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id) {
        teamService.delete(id);
        return "redirect:/teams";
    }
}
