package pl.polsl.mushrooms.application.commands.user;

import pl.polsl.mushrooms.application.commands.VoidCommand;

import javax.validation.constraints.NotNull;

/**
 * Created by pawel_zaqkxkn on 05.06.2017.
 */
public class UpdateProfileImageCommand implements VoidCommand {

    @NotNull
    private final byte[] photo;

    public UpdateProfileImageCommand(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getPhoto() {
        return photo;
    }

}
