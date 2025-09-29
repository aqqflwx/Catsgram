package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.TypeSort;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")

public class PostController {
    private final PostService postService;

    @GetMapping()
    public Collection<Post> findAll(@RequestParam(required = false) int from,
                                    @RequestParam(required = false) int size,
                                    @RequestParam(required = false) TypeSort sort) {
        return postService.findAll(from, size, sort);
    }

    @GetMapping("/{id}")
    public Post findById(@PathVariable(required = false) Long id) {
        return postService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }

}