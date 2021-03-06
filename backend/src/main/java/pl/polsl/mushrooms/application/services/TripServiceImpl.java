package pl.polsl.mushrooms.application.services;

import pl.polsl.mushrooms.application.commands.trip.*;
import pl.polsl.mushrooms.application.dao.NotificationDao;
import pl.polsl.mushrooms.application.dao.TripDao;
import pl.polsl.mushrooms.application.dao.UserDao;
import pl.polsl.mushrooms.application.enums.NotificationType;
import pl.polsl.mushrooms.application.exceptions.EntityAlreadyExistException;
import pl.polsl.mushrooms.application.exceptions.NoRequiredPermissions;
import pl.polsl.mushrooms.application.model.*;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;


public class TripServiceImpl implements TripService {

    private final UserDao userDao;
    private final TripDao tripDao;
    private NotificationDao notificationsDao;

    public TripServiceImpl(final UserDao userDao, final TripDao tripDao, final NotificationDao notificationsDao)
    {
        this.userDao = userDao;
        this.tripDao = tripDao;
        this.notificationsDao = notificationsDao;
    }

    @Override
    public long handle(CreateTripCommand command) {
        final String currentUsername = command.getUserName();
        final Mushroomer user = (Mushroomer) userDao.findOneByUsername(currentUsername)
                .orElseThrow(EntityNotFoundException::new);;

        final Trip trip = new Trip(
                command.getDateTime(),
                command.getPlace(),
                command.getCoordinateX(),
                command.getCoordinateY(),
                command.getRadius());

        trip.addMushroomer(user);
        tripDao.save(trip);

        joinTrip(user, trip);

        return trip.getId();
    }

    @Override
    public void handle(UpdateTripCommand command) {
        final Trip trip = Optional.of(
                tripDao.findTrip(command.getId()))
                    .orElseThrow(NotFoundException::new);

        trip.setPlace(command.getPlace());
        trip.setDateTime(command.getDateTime());
        trip.setCoordinateX(command.getCoordinateX());
        trip.setCoordinateY(command.getCoordinateY());
        trip.setRadius(command.getRadius());
        tripDao.save(trip);
    }

    @Override
    public void handle(DeleteTripCommand command) {
        final String currentUsername = command.getUserName();
        final Mushroomer currentUser = (Mushroomer)userDao.findOneByUsername(currentUsername)
                    .orElseThrow(NotFoundException::new);

        final Trip trip = Optional.of(
                tripDao.findTrip(command.getTripId()))
                    .orElseThrow(NotFoundException::new);

        if (trip.getMushroomers().contains(currentUser)) {
            tripDao.delete(command.getTripId());
        } else {
            throw new NoRequiredPermissions("User should be a participant of the trip.");
        }
    }

    @Override
    public void handle(JoinTripCommand command) {
        final String currentUsername = command.getUserName();
        final Mushroomer mushroomer = (Mushroomer)userDao.findOneByUsername(currentUsername)
                .orElseThrow(NotFoundException::new);

        final Trip trip = Optional.ofNullable(
                tripDao.findTrip(command.getTripId()))
                .orElseThrow(NotFoundException::new);

       joinTrip(mushroomer, trip);
    }

    private void joinTrip(final Mushroomer mushroomer, final Trip trip) {
        final UsersTripsId usersTripsId = new UsersTripsId(mushroomer, trip);

        final UsersTrips usersTrips = Optional.ofNullable(
                tripDao.findUserTrip(usersTripsId))
                .orElseThrow(NotFoundException::new);

        if (usersTrips.getDateTime() == null) {
            usersTrips.setDateTime(LocalDateTime.now());
            tripDao.save(usersTrips);
        } else {
            throw new EntityAlreadyExistException("User already join to the trip");
        }
    }

    @Override
    public void handle(InviteToTripCommand command) {
        final String currentUsername = command.getUserName();
        final Mushroomer mushroomer = (Mushroomer)userDao.findOneByUsername(currentUsername)
                .orElseThrow(() -> {
                    return new EntityNotFoundException(String.format("User with username=$s not found.", currentUsername));
                });

        final Trip trip = Optional.ofNullable(
                tripDao.findTrip(command.getTripId()))
                .orElseThrow(NotFoundException::new);

        for (Long userId : command.getUserIds()) {
            final Optional<User> user = userDao.findOne(userId);
            user.ifPresent(u -> inviteToTrip(trip, u, mushroomer));
        }

        tripDao.save(trip);
    }

    private void inviteToTrip(final Trip trip, final User user, final User invitingPerson) {
        if (user.isMushroomer()) {
            final Mushroomer mushroomer = (Mushroomer)user;
            trip.addMushroomer(mushroomer);
            notificationsDao.save(mushroomer.addNotification(trip.getId(), NotificationType.TRIP_ADDING, invitingPerson));
        } else {
            // log
        }
    }

}
