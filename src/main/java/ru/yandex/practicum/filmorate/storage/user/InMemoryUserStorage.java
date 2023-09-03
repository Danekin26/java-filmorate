package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

import static ru.yandex.practicum.filmorate.validation.QueryValidation.validateUser;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> idAndUser = new HashMap<>();
    private int nextId = 1;

    @Override
    public User addUser(User user) {
        validateUser(user);
        idAndUser.put(nextId, user);
        user.setId(nextId);
        nextId++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        int idUser = user.getId();
        if (idAndUser.containsKey(idUser)) {
            idAndUser.put(idUser, user);
            return user;
        } else {
            log.debug("При обновлении пользователя, вызывается несуществующий идентификатор");
            throw new ValidationException("Пользователя с id = " + user.getId() + " не существует.");
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(idAndUser.values());
    }

    @Override
    public User addUserToFriends(int idUser, int idPotentialFriend) {
        validateUser(getUserById(idUser));
        validateUser(getUserById(idPotentialFriend));
        getUserById(idUser).getFriends().add(idPotentialFriend);
        return getUserById(idUser);
    }

    @Override
    public User removeUserToFriends(int idUser, int idExFriend) {
        validateUser(getUserById(idUser));
        validateUser(getUserById(idExFriend));
        getUserById(idUser).getFriends().remove(idExFriend);
        return getUserById(idUser);
    }

    /*
        На входе 2 сета со списками друзей интересующих пользователей. Создается еще 2 сета, в первый вначале записываются
        все друзья обоих пользователей, во второй записывается только первый пользователь. Затем циклом проходимся по
        списку второго пользователя для удаления повторяющихся. На выходе получаем сет, который пуст, если коллекции
        одинаковые, в противном случае - будет различие списков друзей. И в конце вычитаем из сета со списком всех
        друзей, получившийся сет.
     */
    @Override
    public List<User> getListCommonFriend(int idUser1, int idUser2) {
        Set<Integer> friendsUser1 = collectionFriends(idUser1);
        Set<Integer> friendsUser2 = collectionFriends(idUser2);
        Set<Integer> friendMatches = new HashSet<>();
        friendMatches.addAll(friendsUser1);
        friendMatches.addAll(friendsUser2);
        Set<Integer> varianceListFriends = new HashSet<>(friendsUser1);
        for (Integer id : friendsUser2) {
            if (varianceListFriends.contains(id)) {
                varianceListFriends.remove(id);
            } else {
                varianceListFriends.add(id);
            }
        }
        friendMatches.removeAll(varianceListFriends);

        List<User> listUserFriend = new ArrayList<>();
        for (Integer id : friendMatches) {
            listUserFriend.add(idAndUser.get(id));
        }
        return listUserFriend;
    }

    @Override
    public User getUserById(int idUser) {
        validateUser(idAndUser.get(idUser));
        return idAndUser.get(idUser);
    }

    @Override
    public Set<Integer> collectionFriends(int idUser) {
        return getUserById(idUser).getFriends();
    }
}
