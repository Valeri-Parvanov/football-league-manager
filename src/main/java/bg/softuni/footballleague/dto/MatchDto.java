package bg.softuni.footballleague.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class MatchDto {

    private UUID id;

    @NotNull(message = "Please select a home team")
    private UUID homeTeamId;

    @NotNull(message = "Please select an away team")
    private UUID awayTeamId;

    @PositiveOrZero(message = "Home score cannot be negative")
    private Integer homeScore;

    @PositiveOrZero(message = "Away score cannot be negative")
    private Integer awayScore;

    @NotNull(message = "Please select a date and time")
    private LocalDateTime playedAt;

    private String homeTeamName;

    private String awayTeamName;
}
