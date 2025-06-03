//package vn.fu_ohayo;
//
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.FluentQuery;
//import vn.fu_ohayo.entity.User;
//import vn.fu_ohayo.repository.UserRepository;
//import vn.fu_ohayo.service.PasswordForgotService;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.function.Function;
//
//public class PasswordResetTestMain {
//    public static void main(String[] args) {
//        // In-memory UserRepository for testing
//        UserRepository userRepository = new UserRepository() {
//            @Override
//            public List<User> findAll(Sort sort) {
//                return List.of();
//            }
//
//            @Override
//            public Page<User> findAll(Pageable pageable) {
//                return null;
//            }
//
//            @Override
//            public <S extends User> List<S> saveAll(Iterable<S> entities) {
//                return List.of();
//            }
//
//            @Override
//            public Optional<User> findById(Long aLong) {
//                return Optional.empty();
//            }
//
//            @Override
//            public boolean existsById(Long aLong) {
//                return false;
//            }
//
//            @Override
//            public List<User> findAll() {
//                return List.of();
//            }
//
//            @Override
//            public List<User> findAllById(Iterable<Long> longs) {
//                return List.of();
//            }
//
//            @Override
//            public long count() {
//                return 0;
//            }
//
//            @Override
//            public void deleteById(Long aLong) {
//
//            }
//
//            @Override
//            public void delete(User entity) {
//
//            }
//
//            @Override
//            public void deleteAllById(Iterable<? extends Long> longs) {
//
//            }
//
//            @Override
//            public void deleteAll(Iterable<? extends User> entities) {
//
//            }
//
//            @Override
//            public void deleteAll() {
//
//            }
//
//            @Override
//            public void flush() {
//
//            }
//
//            @Override
//            public <S extends User> S saveAndFlush(S entity) {
//                return null;
//            }
//
//            @Override
//            public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
//                return List.of();
//            }
//
//            @Override
//            public void deleteInBatch(Iterable<User> entities) {
//                UserRepository.super.deleteInBatch(entities);
//            }
//
//            @Override
//            public void deleteAllInBatch(Iterable<User> entities) {
//
//            }
//
//            @Override
//            public void deleteAllByIdInBatch(Iterable<Long> longs) {
//
//            }
//
//            @Override
//            public void deleteAllInBatch() {
//
//            }
//
//            @Override
//            public User getOne(Long aLong) {
//                return null;
//            }
//
//            @Override
//            public User getById(Long aLong) {
//                return null;
//            }
//
//            @Override
//            public User getReferenceById(Long aLong) {
//                return null;
//            }
//
//            @Override
//            public <S extends User> Optional<S> findOne(Example<S> example) {
//                return Optional.empty();
//            }
//
//            @Override
//            public <S extends User> List<S> findAll(Example<S> example) {
//                return List.of();
//            }
//
//            @Override
//            public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
//                return List.of();
//            }
//
//            @Override
//            public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
//                return null;
//            }
//
//            @Override
//            public <S extends User> long count(Example<S> example) {
//                return 0;
//            }
//
//            @Override
//            public <S extends User> boolean exists(Example<S> example) {
//                return false;
//            }
//
//            @Override
//            public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
//                return null;
//            }
//
//            private final Map<String, User> users = new HashMap<>();
//            @Override public Optional<User> findByEmail(String email) { return Optional.ofNullable(users.get(email)); }
//
//            @Override
//            public Optional<User> findByUsername(String username) {
//                return Optional.empty();
//            }
//
//            @Override
//            public Optional<User> findByEmailOrUsername(String email, String username) {
//                return Optional.empty();
//            }
//
//            @Override
//            public boolean existsByEmail(String email) {
//                return false;
//            }
//
//            @Override
//            public boolean existsByUsername(String username) {
//                return false;
//            }
//
//            @Override public <S extends User> S save(S entity) { users.put(entity.getEmail(), entity); return entity; }
//            // Other methods can be left unimplemented for this test
//        };
//
//        // Inject repository via reflection
//        try {
//            var field = PasswordForgotService.class.getDeclaredField("userRepository");
//            field.setAccessible(true);
//            field.set(passwordForgotService, userRepository);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        // Add a test user
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPassword(passwordForgotService.hashPassword("oldPassword"));
//        userRepository.save(user);
//
//        // Generate and display token
//        passwordForgotService.createAndSendToken("test@example.com");
//
//        // Prompt user for token and new password (with 6-attempt limit)
//        passwordForgotService.userInputPassword();
//    }
//}