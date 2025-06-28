package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.AdminRequest;
import vn.fu_ohayo.dto.response.AdminResponse;

import java.util.List;

public interface AdminService {
    AdminResponse create(AdminRequest req);
    List<AdminResponse> getAll();
    AdminResponse getById(Long id);
    AdminResponse update(Long id, AdminRequest req);
    void delete(Long id);
}
