package bg.softuni.footballleague.repository;

import bg.softuni.footballleague.model.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League, Long> {

    Optional<League> findByName(String name);
}
