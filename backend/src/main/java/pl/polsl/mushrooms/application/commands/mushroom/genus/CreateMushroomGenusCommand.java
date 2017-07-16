package pl.polsl.mushrooms.application.commands.mushroom.genus;

import org.hibernate.validator.constraints.NotEmpty;
import pl.polsl.mushrooms.application.commands.ReturningCommand;

import javax.validation.constraints.NotNull;

/**
 * Created by pawel_zaqkxkn on 16.07.2017.
 */
public class CreateMushroomGenusCommand implements ReturningCommand<Long> {

    @NotNull
    private Long mushroomFamilyId;

    @NotNull
    @NotEmpty
    private String name;

    protected CreateMushroomGenusCommand() { }

    public Long getMushroomFamilyId() {
        return mushroomFamilyId;
    }

    public String getName() {
        return name;
    }
}
