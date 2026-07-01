package bg.softuni.footballleague.repository;

import bg.softuni.footballleague.model.League;
import bg.softuni.footballleague.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {

    List<Team> findAllByLeague(League league);
}
