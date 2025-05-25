package vn.fu_ohayo;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.PRService;

import java.util.*;
import java.util.function.Function;

public class PasswordResetTestMain {
    public static void main(String[] args) {
        // Simple in-memory UserRepository implementation for testing
        UserRepository userRepository = new UserRepository() {
            @Override
            public List<User> findAll(Sort sort) {
                return List.of();
            }

            @Override
            public Page<User> findAll(Pageable pageable) {
                return null;
            }

            private final Map<String, User> users = new HashMap<>();
            @Override public Optional<User> findByEmail(String email) { return Optional.ofNullable(users.get(email)); }
            @Override public <S extends User> S save(S entity) { users.put(entity.getEmail(), entity); return entity; }
            // Implement other methods as needed or throw UnsupportedOperationException
            @Override public Optional<User> findByUsername(String username) { throw new UnsupportedOperationException(); }
            @Override public Optional<User> findByEmailOrUsername(String email, String username) { throw new UnsupportedOperationException(); }
            @Override public boolean existsByEmail(String email) { return users.containsKey(email); }
            @Override public boolean existsByUsername(String username) { throw new UnsupportedOperationException(); }
            @Override public List<User> findAll() { throw new UnsupportedOperationException(); }
            @Override public List<User> findAllById(Iterable<Long> longs) { throw new UnsupportedOperationException(); }
            @Override public Optional<User> findById(Long aLong) { throw new UnsupportedOperationException(); }
            @Override public void deleteById(Long aLong) { throw new UnsupportedOperationException(); }
            @Override public void delete(User entity) { throw new UnsupportedOperationException(); }
            @Override public void deleteAllById(Iterable<? extends Long> longs) { throw new UnsupportedOperationException(); }
            @Override public void deleteAll(Iterable<? extends User> entities) { throw new UnsupportedOperationException(); }
            @Override public void deleteAll() { throw new UnsupportedOperationException(); }
            @Override public long count() { throw new UnsupportedOperationException(); }
            @Override public <S extends User> List<S> saveAll(Iterable<S> entities) { throw new UnsupportedOperationException(); }
            @Override public boolean existsById(Long aLong) { throw new UnsupportedOperationException(); }
            @Override public void flush() { throw new UnsupportedOperationException(); }
            @Override public <S extends User> S saveAndFlush(S entity) { throw new UnsupportedOperationException(); }
            @Override public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) { throw new UnsupportedOperationException(); }
            @Override public void deleteAllInBatch(Iterable<User> entities) { throw new UnsupportedOperationException(); }
            @Override public void deleteAllByIdInBatch(Iterable<Long> longs) { throw new UnsupportedOperationException(); }
            @Override public void deleteAllInBatch() { throw new UnsupportedOperationException(); }
            @Override public User getOne(Long aLong) { throw new UnsupportedOperationException(); }
            @Override public User getById(Long aLong) { throw new UnsupportedOperationException(); }
            @Override public User getReferenceById(Long aLong) { throw new UnsupportedOperationException(); }

            @Override
            public <S extends User> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends User> List<S> findAll(Example<S> example) {
                return List.of();
            }

            @Override
            public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
                return List.of();
            }

            @Override
            public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends User> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends User> boolean exists(Example<S> example) {
                return false;
            }

            @Override
            public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
                return null;
            }
        };

        PRService prService = new PRService();
        // Use reflection to inject the repository (since @Autowired won't work here)
        try {
            var field = PRService.class.getDeclaredField("userRepository");
            field.setAccessible(true);
            field.set(prService, userRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Add a test user
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(prService.hashPassword("oldPassword"));
        userRepository.save(user);

        // Generate token
        prService.createAndSendToken("test@example.com");

        // Get the token (simulate getting from email)
        String token = prService.tokenStore.keySet().iterator().next();

        // Reset password
        boolean result = prService.resetPassword(token, "newPassword");
        System.out.println("Reset result: " + result);

        // Verify password updated
        User updatedUser = userRepository.findByEmail("test@example.com").get();
        System.out.println("New hashed password: " + updatedUser.getPassword());
    }
}