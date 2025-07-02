package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.response.AdminResponse;

public interface AdminService {
    Page<AdminResponse> getAllByName(String searchName, int page, int size);
}
