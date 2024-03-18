package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

/**
 * <p>Data-класс User содержит информацию о сущности "пользователь" и имеет поля:</p>
 * <ul>
 *     <li>id - уникальный идентификатор пользователя, тип Integer;</li>
 *     <li>name - имя пользователя, тип String;</li>
 *     <li>email - адрес электронной почты пользователя, тип String.</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
}
