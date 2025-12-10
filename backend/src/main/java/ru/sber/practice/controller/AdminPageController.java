package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sber.practice.model.User;
import ru.sber.practice.service.UserService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminPageController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getUsersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model) {

        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.ASC, "id"));
        Page<User> userPage;

        if (search != null && !search.trim().isEmpty()) {
            userPage = userService.searchUsers(search.trim(), pageable);
            model.addAttribute("search", search);
        } else {
            userPage = userService.findAllUsers(pageable);
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", userPage.getNumber());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());

        // Рассчитываем диапазон страниц для отображения
        int startPage = Math.max(0, userPage.getNumber() - 2);
        int endPage = Math.min(userPage.getTotalPages() - 1, userPage.getNumber() + 2);

        // Создаем массив страниц для отображения
        int[] pageNumbers = new int[0];
        if (startPage <= endPage) {
            pageNumbers = new int[endPage - startPage + 1];
            for (int i = 0; i < pageNumbers.length; i++) {
                pageNumbers[i] = startPage + i;
            }
        }

        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/users";
    }
}