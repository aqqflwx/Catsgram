package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.TypeSort;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;

    public Collection<Post> findAll(int from, int size, TypeSort sort) {
        if (from == 0 && size == 0) {
            size = 10;
        }

        if (from < 0) {
            throw new ConditionsNotMetException("Количество отбрасываемых постов должно быть положительным значением");
        }

        if (size <= 0) {
            throw new ConditionsNotMetException("Количество отображаемых постов должно быть больше 0");
        }

        return posts.values().stream()
                .sorted((p1, p2) -> {
                    switch (sort) {
                        case ASC -> {
                            return p1.getPostDate().compareTo(p2.getPostDate());
                        }
                        case DESC -> {
                            return p2.getPostDate().compareTo(p1.getPostDate());
                        }
                        default -> {
                            return 0;
                        }
                    }
                })
                .skip(from)
                .limit(size)
                .toList();
    }

    public Post findById(Long id) {
        if (posts.get(id) == null) {
            throw new ConditionsNotMetException("Поста с id = " + id + " не найдено");
        }

        return posts.get(id);
    }

    public Post create(Post post) {
        if (userService.findUserById(post.getAuthorId()).isEmpty()) {
            throw new ConditionsNotMetException("Автор с id = " + post.getAuthorId() + " не найден");
        }

        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());

        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
