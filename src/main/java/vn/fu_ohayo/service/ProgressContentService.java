package vn.fu_ohayo.service;

import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.entity.ContentListening;

public interface ProgressContentService {
    ProgressContent saveUserListeningProgress(User user, ContentListening contentListening, int correct_answers, int total_questions);
    ProgressContent saveUserReadingProgress(User user, ContentReading contentReading, int progress);
}