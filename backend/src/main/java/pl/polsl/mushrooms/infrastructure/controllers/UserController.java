package pl.polsl.mushrooms.infrastructure.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.mushrooms.application.commands.user.CreateUserCommand;
import pl.polsl.mushrooms.application.commands.user.DeleteUsersCommand;
import pl.polsl.mushrooms.application.commands.user.UpdateProfileImageCommand;
import pl.polsl.mushrooms.application.commands.user.UpdateUserCommand;
import pl.polsl.mushrooms.application.services.projections.UserProjectionService;
import pl.polsl.mushrooms.infrastructure.commands.CommandGateway;
import pl.polsl.mushrooms.infrastructure.dto.MushroomerDto;
import pl.polsl.mushrooms.infrastructure.dto.UserDto;

import java.io.IOException;
import java.util.Set;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserProjectionService userProjectionService;
    private final CommandGateway commandGateway;


    @Autowired
    public UserController(
            UserProjectionService userProjectionService, CommandGateway commandGateway) {
        this.userProjectionService = userProjectionService;
        this.commandGateway = commandGateway;
    }

    /**
     * Rest method to create an user
     * @param command to create an user
     * @return Id of the new user
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Long> create(@RequestBody CreateUserCommand command) {
        final long id = commandGateway.dispatch(command);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    /**
     * Get user by id
     * @param id of the user
     * @return details of the user
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getById(@PathVariable(name = "id") long id) {
        final UserDto user = userProjectionService.findOne(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Get current user details
     * @return details of current logged user
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<UserDto> get() {
        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        final UserDto user = userProjectionService.findOneByUsername(userName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Get all users. Admin role is required
     * @return array of the all users
     */
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public ResponseEntity<Set<MushroomerDto>> getAll() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        final Set<MushroomerDto> users = userProjectionService.findAll(username);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Search an user by name
     * @param value part of the user's name
     * @return array of the found users
     */
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ResponseEntity<Set<MushroomerDto>> search(@RequestParam(value = "value") String value) {
        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        final Set<MushroomerDto> users = userProjectionService.search(userName, value);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * UPDATE
     * @param command
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<UserDto> update(@RequestBody UpdateUserCommand command) {
        command.setCurrentUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        final UserDto user = commandGateway.dispatch(command);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * DELETE
     * @param command
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestBody DeleteUsersCommand command) {
        command.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        commandGateway.dispatch(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * UPDATE PROFILE IMAGE
     * @param image
     * @return
     */
    @RequestMapping(path = "image", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> image(@RequestParam("files") MultipartFile image) {
        try {

            final UpdateProfileImageCommand command = new UpdateProfileImageCommand(image.getBytes());
            command.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            commandGateway.dispatch(command);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
