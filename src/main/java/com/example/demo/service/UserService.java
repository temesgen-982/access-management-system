package com.example.demo.service;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.event.AuditActionEvt;
import com.example.demo.model.Department;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final DepartmentRepository departmentRepository;
  private final PasswordEncoder passwordEncoder;
  private final ApplicationEventPublisher eventPublisher;

  public UserService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      DepartmentRepository departmentRepository,
      PasswordEncoder passwordEncoder,
      ApplicationEventPublisher eventPublisher) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.departmentRepository = departmentRepository;
    this.passwordEncoder = passwordEncoder;
    this.eventPublisher = eventPublisher;
  }

  public UserResponse createUser(CreateUserRequest request) {
    // Enforce business rules: uniqueness validation
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new RuntimeException("Email already exists");
    }

    // Fetch and validate the clinical department
    Department department =
        departmentRepository
            .findById(request.getDepartmentId())
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "Department not found with ID: " + request.getDepartmentId()));

    // Fetch and validate the default security role
    Role defaultRole =
        roleRepository
            .findByName("ROLE_USER")
            .orElseThrow(() -> new RuntimeException("Error: Default User Role not initialized."));

    // Build the new User domain model
    User user =
        User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .roles(Set.of(defaultRole))
            .department(department) // Map the department reference directly
            .build();

    User savedUser = userRepository.save(user);

    String actor =
        SecurityContextHolder.getContext().getAuthentication() != null
            ? SecurityContextHolder.getContext().getAuthentication().getName()
            : "SYSTEM";

    eventPublisher.publishEvent(
        new AuditActionEvt(
            "USER_REGISTRATION",
            actor,
            "Registered new user: "
                + savedUser.getEmail()
                + " inside department: "
                + savedUser.getDepartment().getName()));

    return mapToResponse(savedUser);
  }

  public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
  }

  public UserResponse getUserById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    return mapToResponse(user);
  }

  public void deleteUser(Long id) {
    User userToDelete =
        userRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

    String currentActor = SecurityContextHolder.getContext().getAuthentication().getName();

    eventPublisher.publishEvent(
        new AuditActionEvt(
            "USER_DELETION",
            currentActor,
            "Deleted user account: " + userToDelete.getEmail() + " (ID: " + id + ")"));

    userRepository.deleteById(id);
  }

  // Centralized mapping utility to format output presentation cleanly
  private UserResponse mapToResponse(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .departmentName(
            user.getDepartment() != null ? user.getDepartment().getName() : "Unassigned")
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }
}
