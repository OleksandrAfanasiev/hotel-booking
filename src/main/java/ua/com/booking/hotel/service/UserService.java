package ua.com.booking.hotel.service;

import ua.com.booking.hotel.entity.User;

import java.util.Optional;

public interface UserService {

    User create(User user);

    Optional<User> getUserById(long id);
}
