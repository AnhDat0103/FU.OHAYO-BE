package vn.fu_ohayo.service;

import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.entity.ContentListening;

public interface ProgressContentService {
    ProgressContent saveUserProgress(User user, ContentListening contentListening, int progress);
}