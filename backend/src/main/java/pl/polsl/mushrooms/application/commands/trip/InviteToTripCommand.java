package pl.polsl.mushrooms.application.commands.trip;

import pl.polsl.mushrooms.application.commands.VoidCommand;

import javax.validation.constraints.NotNull;

/**
 * Created by pawel_zaqkxkn on 27.06.2017.
 */
public class InviteToTripCommand implements VoidCommand {

    @NotNull
    private Long userId;

    @NotNull
    private Long tripId;

    protected InviteToTripCommand() { }

    public Long getUserId() {
        return userId;
    }

    public Long getTripId() {
        return tripId;
    }
}