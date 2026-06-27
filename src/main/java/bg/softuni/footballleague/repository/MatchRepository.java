package bg.softuni.footballleague.repository;

import bg.softuni.footballleague.model.Match;
import bg.softuni.footballleague.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findAllByHomeTeamOrAwayTeam(Team homeTeam, Team awayTeam);
}
