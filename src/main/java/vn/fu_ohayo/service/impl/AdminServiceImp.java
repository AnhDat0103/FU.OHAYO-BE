package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.AdminRequest;
import vn.fu_ohayo.dto.response.AdminResponse;
import vn.fu_ohayo.dto.response.RoleResponse;
import vn.fu_ohayo.entity.Admin;
import vn.fu_ohayo.entity.Role;
import vn.fu_ohayo.enums.RoleEnum;
import vn.fu_ohayo.repository.AdminRepository;
import vn.fu_ohayo.repository.RoleRepository;
import vn.fu_ohayo.service.AdminService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImp implements AdminService {
    private final AdminRepository adminRepo;
    private final RoleRepository roleRepo;

    @Override
    public AdminResponse create(AdminRequest req) {
        if (adminRepo.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }
//        Set<RoleEnum> roles = roleRepo.findAllById(req.getRoleIds()).stream().collect(Collectors.toSet());
//        Admin admin = Admin.builder()
//                .username(req.getUsername())
//                .password(req.getPassword()) // bạn nên mã hóa trước khi lưu
////                .roles(roles)
//                .build();
//        admin = adminRepo.save(admin);
        return null;
    }

    @Override
    public List<AdminResponse> getAll() {
        return adminRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public AdminResponse getById(Long id) {
        Admin admin = adminRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin không tìm thấy"));
        return toResponse(admin);
    }

    @Override
    public AdminResponse update(Long id, AdminRequest req) {
        Admin admin = adminRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin không tìm thấy"));
        admin.setUsername(req.getUsername());
        admin.setPassword(req.getPassword()); // nhớ mã hóa
        Set<Role> roles = roleRepo.findAllById(req.getRoleIds()).stream().collect(Collectors.toSet());
        admin.setRoles(roles);
        return toResponse(adminRepo.save(admin));
    }

    @Override
    public void delete(Long id) {
        adminRepo.deleteById(id);
    }

    private AdminResponse toResponse(Admin a) {
        Set<RoleResponse> roleRes = a.getRoles().stream().map(r ->
                RoleResponse.builder()
                        .roleId(r.getRoleId())
                        .name(r.getName())
                        .description(r.getDescription())
                        .permissions(
                                r.getPermissions().stream()
                                        .map(p -> p.getName().name())  // hoặc tùy cách bạn định nghĩa
                                        .collect(Collectors.toSet())
                        )
                        .build()
        ).collect(Collectors.toSet());

        return AdminResponse.builder()
                .adminId(a.getAdminId())
                .username(a.getUsername())
                .roles(roleRes)
                .build();
    }
}
