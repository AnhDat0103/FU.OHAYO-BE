package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Grammar;
import vn.fu_ohayo.entity.ProgressGrammar;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.List;

@Repository
public interface ProgressGrammarRepository extends JpaRepository<ProgressGrammar, Integer> {

    List<ProgressGrammar> findAllByUserAndProgressStatusAndGrammarIn(User user, ProgressStatus progressStatus, List<Grammar> grammars);
    List<ProgressGrammar> findAllByUserAndGrammarIn(User user, List<Grammar> grammars);
}
