package pl.polsl.mushrooms.application.services;

import pl.polsl.mushrooms.application.commands.trip.*;
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

/**
 * Created by pawel_zaqkxkn on 24.04.2017.
 */
public class TripServiceImpl implements TripService {

    private final UserDao userDao;
    private final TripDao tripDao;

    public TripServiceImpl(final UserDao userDao, final TripDao tripDao)
    {
        this.userDao = userDao;
        this.tripDao = tripDao;
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

        return trip.getId();
    }

    @Override
    public void handle(UpdateTripCommand command) {
        final Trip trip = Optional.of(
                tripDao.findTrip(command.getTripId()))
                    .orElseThrow(NotFoundException::new);

        trip.setPlace(command.getPlace());
        trip.setDateTime(command.getDateTime());
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

        final Trip trip = Optional.ofNullable(
                tripDao.findTrip(command.getTripId()))
                .orElseThrow(NotFoundException::new);

        for (Long userId : command.getUserIds()) {
            final Mushroomer mushroomer = (Mushroomer) userDao.findOne(userId);

            if (mushroomer != null) {
                trip.addMushroomer(mushroomer);

                final User userOfContent = userDao.findOneByUsername(command.getUserName())
                        .orElseThrow(EntityNotFoundException::new);;
                mushroomer.addNotification(trip.getId(), NotificationType.TRIP_ADDING, userOfContent);
            }
        }

        tripDao.save(trip);
    }

}
