package bg.softuni.footballleague.repository;

import bg.softuni.footballleague.model.Goal;
import bg.softuni.footballleague.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, UUID> {

    List<Goal> findAllByMatchOrderByHalfAscMinuteAsc(Match match);
}
