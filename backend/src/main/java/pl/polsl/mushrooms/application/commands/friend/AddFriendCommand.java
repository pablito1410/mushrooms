package pl.polsl.mushrooms.application.commands.friend;

import pl.polsl.mushrooms.application.commands.ReturningCommand;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Created by pawel_zaqkxkn on 22.05.2017.
 */
public class AddFriendCommand implements ReturningCommand<Collection<Long>> {

    @NotNull
    private long[] friendIds;

    protected AddFriendCommand() { }

    public long[] getFriendIds() {
        return friendIds;
    }
}
