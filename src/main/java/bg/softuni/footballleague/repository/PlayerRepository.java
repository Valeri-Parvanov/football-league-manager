package bg.softuni.footballleague.repository;

import bg.softuni.footballleague.model.Player;
import bg.softuni.footballleague.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findAllByTeam(Team team);
}
