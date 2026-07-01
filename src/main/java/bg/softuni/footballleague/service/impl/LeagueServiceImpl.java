package bg.softuni.footballleague.service.impl;

import bg.softuni.footballleague.dto.LeagueDto;
import bg.softuni.footballleague.exception.EntityNotFoundException;
import bg.softuni.footballleague.model.League;
import bg.softuni.footballleague.model.Match;
import bg.softuni.footballleague.model.Team;
import bg.softuni.footballleague.repository.LeagueRepository;
import bg.softuni.footballleague.repository.MatchRepository;
import bg.softuni.footballleague.service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeagueServiceImpl implements LeagueService {

    private static final Sort DEFAULT_SORT = Sort.by("name");

    private final LeagueRepository leagueRepository;
    private final MatchRepository matchRepository;

    @Override
    public List<LeagueDto> findAll() {
        return findAll(DEFAULT_SORT);
    }

    @Override
    public List<LeagueDto> findAll(Sort sort) {
        return leagueRepository.findAll(sort).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public LeagueDto findById(UUID id) {
        return toDto(getLeagueOrThrow(id));
    }

    @Override
    public LeagueDto create(LeagueDto leagueDto) {
        League league = new League();
        mapToEntity(leagueDto, league);
        return toDto(leagueRepository.save(league));
    }

    @Override
    public LeagueDto update(UUID id, LeagueDto leagueDto) {
        League league = getLeagueOrThrow(id);
        mapToEntity(leagueDto, league);
        return toDto(leagueRepository.save(league));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        League league = getLeagueOrThrow(id);
        for (Team team : league.getTeams()) {
            List<Match> matches = matchRepository.findAllByHomeTeamOrAwayTeam(team, team);
            matchRepository.deleteAll(matches);
        }
        leagueRepository.delete(league);
    }

    private League getLeagueOrThrow(UUID id) {
        return leagueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("League with id %s not found".formatted(id)));
    }

    private void mapToEntity(LeagueDto leagueDto, League league) {
        league.setName(leagueDto.getName());
        league.setCountry(leagueDto.getCountry());
    }

    private LeagueDto toDto(League league) {
        LeagueDto leagueDto = new LeagueDto();
        leagueDto.setId(league.getId());
        leagueDto.setName(league.getName());
        leagueDto.setCountry(league.getCountry());
        return leagueDto;
    }
}
