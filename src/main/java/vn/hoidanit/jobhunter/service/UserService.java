package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserUpdateDTO;

public interface UserService {
    ResUserCreateDTO create(User user);

    ResUserDTO findOne(long id);

    PaginationResultDTO findAll(Specification<User> spec, Pageable pageable);

    ResUserUpdateDTO update(ResUserUpdateDTO user);

    void delete(long id);

    User findByEmail(String email);

    boolean isEmailExist(String email);

    void updateUserToken(String token, String email);

    User getUserByRefreshTokenAndEmail(String refreshToken, String email);

    // long countUserByCompanyId(long companyId);
}
