package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("labSecurityExpression")
@RequiredArgsConstructor
public class LabSecurityExpression {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public boolean canAccessUser(Object principal, Long targetUserId) {
    if (!(principal instanceof User currentUser)) {
      return false;
    }

    boolean isAdmin =
        currentUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMIN"));

    if (isAdmin) {
      return true;
    }

    Optional<User> targetUserOpt = userRepository.findById(targetUserId);
    if (targetUserOpt.isEmpty()) {
      return true;
    }

    User targetUser = targetUserOpt.get();

    Long currentDeptId = currentUser.getDepartmentId();
    Long targetDeptId = targetUser.getDepartmentId();

    if (currentDeptId == null || targetDeptId == null) {
      return false;
    }

    return currentDeptId.equals(targetDeptId);
  }
}
