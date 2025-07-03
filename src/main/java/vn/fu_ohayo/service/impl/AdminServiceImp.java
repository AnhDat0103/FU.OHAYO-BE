package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.AdminResponse;
import vn.fu_ohayo.dto.response.RoleResponse;
import vn.fu_ohayo.repository.AdminRepository;
import vn.fu_ohayo.repository.RoleRepository;
import vn.fu_ohayo.service.AdminService;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImp implements AdminService {
    private final AdminRepository adminRepo;

    @Override
    public Page<AdminResponse> getAllByName(String searchName, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        return adminRepo.findByUsernameContainingIgnoreCase(searchName, pageable).map(admin -> AdminResponse.builder()
                .adminId(admin.getAdminId())
                .username(admin.getUsername())
                .roles(admin.getRoles().stream().map(role -> RoleResponse.builder()
                        .roleId(role.getRoleId())
                        .name(role.getName())
                        .description(role.getDescription())
                        .permissions(role.getPermissions().stream()
                                .map(permission -> permission.getName().name())
                                .collect(Collectors.toSet()))
                        .build()).collect(Collectors.toSet()))
                .build());
    }

}
