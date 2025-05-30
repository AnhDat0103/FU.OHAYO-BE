package vn.fu_ohayo.aspect;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import vn.fu_ohayo.entity.SystemLog;
import vn.fu_ohayo.enums.RoleEnum;
import vn.fu_ohayo.repository.SystemLogRepository;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SystemLogAspect {
    SystemLogRepository systemLogRepository;

    @Pointcut("execution(* vn.fu_ohayo.controller..*(..)) || execution(* vn.fu_ohayo.service..*(..))")
    public void appPackagePointcut() {}

    @AfterReturning(pointcut = "appPackagePointcut()", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        RoleEnum role = RoleEnum.SUPER_ADMIN;

        String methodName = joinPoint.getSignature().toShortString();

        methodName = methodName.replace("Controller", "");
        methodName = methodName.replace("Service", "");
        methodName = methodName.replace("Imp", "");
        methodName = methodName.replaceAll("\\(\\.\\.\\)", "");

        String action = methodName.replace(".", " ");

        if (action.equals("SystemLog searchSystemLog")) {
            return;
        }

        String details = String.format("%s action performed by '%s' with result: %s",
                action, role, result != null ? result.toString() : "null");

        SystemLog systemLog = SystemLog.builder()
                .action(action)
                .details(details)
                .role(role)
                .timestamp(LocalDateTime.now())
                .build();

        systemLogRepository.save(systemLog);

        log.info("Logged action: {}, details: {}, role: {}", action, details, role);
    }

}

