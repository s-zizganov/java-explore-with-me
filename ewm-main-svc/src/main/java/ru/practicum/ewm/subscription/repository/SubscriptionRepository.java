package ru.practicum.ewm.subscription.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.subscription.model.Subscription;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utils.FriendshipsStatus;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью подписки.
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Находит подписку по подписчику и владельцу.
     *
     * @param follower подписчик
     * @param owner    владелец
     * @return объект {@link Optional} с подпиской, если найдена
     */
    Optional<Subscription> findByFollowerAndOwner(User follower, User owner);

    /**
     * Находит все подписки для указанного подписчика.
     *
     * @param follower подписчик
     * @return список подписок
     */
    List<Subscription> findByFollower(User follower);

    /**
     * Находит подписки по владельцу с пагинацией.
     *
     * @param owner    владелец
     * @param pageable параметры пагинации
     * @return список подписок
     */
    List<Subscription> findByOwner(User owner, Pageable pageable);

    /**
     * Подсчитывает количество подписчиков владельца с указанными статусами дружбы.
     *
     * @param owner               владелец
     * @param friendshipsStatuses список статусов дружбы
     * @return количество подписчиков
     */
    long countByOwnerAndFriendshipsStatusIn(User owner, List<FriendshipsStatus> friendshipsStatuses);
}