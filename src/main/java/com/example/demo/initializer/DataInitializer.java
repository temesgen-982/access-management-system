// Because we are using an in-memory H2 database, our database tables clear every single time the
// application restarts. To prevent having to manually recreate the ROLE_USER and ROLE_ADMIN records
// on every run, we can write a tiny startup seeder class using Spring's CommandLineRunner.
package com.example.demo.initializer;

import com.example.demo.model.Department;
import com.example.demo.model.Permission;
import com.example.demo.model.Role;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final DepartmentRepository departmentRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    // 1. Seed Fine-Grained Permissions
    Permission viewUsers = savePermissionIfAbsent("VIEW_USERS");
    Permission createUser = savePermissionIfAbsent("CREATE_USER");
    Permission deleteUser = savePermissionIfAbsent("DELETE_USER");
    Permission viewAuditLogs = savePermissionIfAbsent("VIEW_AUDIT_LOGS");

    // 2. Seed Roles
    saveRoleIfAbsent("ROLE_USER", Set.of(viewUsers));
    saveRoleIfAbsent("ROLE_ADMIN", Set.of(viewUsers, createUser, deleteUser, viewAuditLogs));

    // 3. Seed LIS Departments
    saveDepartmentIfAbsent("Pathology");
    saveDepartmentIfAbsent("Hematology");
    saveDepartmentIfAbsent("Microbiology");
    saveDepartmentIfAbsent("Biochemistry");
  }

  private Permission savePermissionIfAbsent(String name) {
    return permissionRepository
        .findByName(name)
        .orElseGet(() -> permissionRepository.save(Permission.builder().name(name).build()));
  }

  private void saveRoleIfAbsent(String name, Set<Permission> permissions) {
    roleRepository
        .findByName(name)
        .ifPresentOrElse(
            existingRole -> {
              existingRole.setPermissions(permissions);
              roleRepository.save(existingRole);
            },
            () -> {
              Role newRole = Role.builder().name(name).permissions(permissions).build();
              roleRepository.save(newRole);
            });
  }

  private void saveDepartmentIfAbsent(String name) {
    if (departmentRepository.findByName(name).isEmpty()) {
      departmentRepository.save(Department.builder().name(name).build());
    }
  }
}
