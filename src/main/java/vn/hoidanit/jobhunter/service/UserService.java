package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Pageable;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;

public interface UserService {
    User create(User user);

    User findOne(long id);

    PaginationResultDTO findAll(Pageable pageable);

    User update(User user);

    void delete(long id);

    User findByEmail(String email);
}
