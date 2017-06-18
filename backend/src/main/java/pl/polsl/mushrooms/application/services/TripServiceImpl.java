package pl.polsl.mushrooms.application.services;

import org.springframework.security.core.context.SecurityContextHolder;
import pl.polsl.mushrooms.application.commands.trip.CreateTripCommand;
import pl.polsl.mushrooms.application.commands.trip.DeleteTripCommand;
import pl.polsl.mushrooms.application.commands.trip.UpdateTripCommand;
import pl.polsl.mushrooms.application.dao.TripDao;
import pl.polsl.mushrooms.application.dao.UserDao;
import pl.polsl.mushrooms.application.exceptions.NoRequiredPermissions;
import pl.polsl.mushrooms.application.model.Mushroomer;
import pl.polsl.mushrooms.application.model.Trip;

/**
 * Created by pawel_zaqkxkn on 24.04.2017.
 */
public class TripServiceImpl implements TripService {

    private final UserDao userRepo;
    private final TripDao tripRepo;

    public TripServiceImpl(final UserDao userDao, final TripDao tripDao)
    {
        this.userRepo = userDao;
        this.tripRepo = tripDao;
    }

    @Override
    public long handle(CreateTripCommand command) {
        final Trip trip = new Trip(command.getDateTime(), command.getPlace());
        final String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        final Mushroomer user = (Mushroomer)userRepo.findOneByUsername(currentUsername);
        trip.addMushroomer(user);

        tripRepo.save(trip);
        return trip.getId();
    }

    @Override
    public void handle(UpdateTripCommand command) {
        final Trip trip = tripRepo.findTrip(command.getTripId());
        trip.setPlace(command.getPlace());
        trip.setDateTime(command.getDateTime());
        tripRepo.save(trip);
    }

    @Override
    public void handle(DeleteTripCommand command) {
        final String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        final Mushroomer currentUser = (Mushroomer)userRepo.findOneByUsername(currentUsername);
        final Trip trip = tripRepo.findTrip(command.getTripId());

        if (trip.getMushroomers().contains(currentUser)) {
            tripRepo.delete(command.getTripId());
        } else {
            throw new NoRequiredPermissions("User should be a participant of the trip.");
        }
    }

}
