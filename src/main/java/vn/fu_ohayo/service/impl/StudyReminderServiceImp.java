package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.StudyReminderRequest;
import vn.fu_ohayo.dto.response.StudyReminderResponse;
import vn.fu_ohayo.entity.StudyReminder;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.StudyReminderMapper;
import vn.fu_ohayo.repository.StudyReminderRepository;
import vn.fu_ohayo.service.StudyReminderService;
import vn.fu_ohayo.service.UserService;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudyReminderServiceImp implements StudyReminderService {

    private final StudyReminderRepository studyReminderRepository;
    private final UserService userService;
    private final StudyReminderMapper studyReminderMapper;

    public StudyReminderServiceImp(StudyReminderRepository studyReminderRepository, UserService userService, StudyReminderMapper studyReminderMapper) {
        this.studyReminderRepository = studyReminderRepository;
        this.userService = userService;
        this.studyReminderMapper = studyReminderMapper;
    }

    @Override
    public List<StudyReminderResponse> getStudyRemindersByUserId(long userId) {
        User user =userService.getUserById(userId);
        return studyReminderRepository.findAllByUser(user).stream().map(studyReminderMapper::toStudyReminderResponse).collect(Collectors.toList());    }

    @Override
    public StudyReminderResponse addStudyReminder(StudyReminderRequest studyReminderRequest, long userId) {
        StudyReminder studyReminder = StudyReminder.builder()
                .note(studyReminderRequest.getNote())
                .time(studyReminderRequest.getTime())
                .isActive(studyReminderRequest.getIsActive())
                .daysOfWeek(studyReminderRequest.getDaysOfWeek())
                .user(userService.getUserById(userId))
                .build();
        return studyReminderMapper.toStudyReminderResponse(studyReminderRepository.save(studyReminder));
    }

    @Override
    public StudyReminderResponse updateStudyReminder(int studyReminderId, StudyReminderRequest request) {
        StudyReminder studyReminder = getStudyReminderById(studyReminderId);
        if (request.getNote() != null) {
            studyReminder.setNote(request.getNote());
        }
        if (request.getTime() != null) {
            studyReminder.setTime(request.getTime());
        }
        if (request.getIsActive() != null) {
            studyReminder.setIsActive(request.getIsActive());
        }
        if (request.getDaysOfWeek() != null) {
            studyReminder.setDaysOfWeek(request.getDaysOfWeek());
        }
        return studyReminderMapper.toStudyReminderResponse(studyReminderRepository.save(studyReminder));
    }

    @Override
    public void deleteStudyReminder(int studyReminderId) {
        StudyReminder studyReminder = getStudyReminderById(studyReminderId);
        studyReminderRepository.delete(studyReminder);
    }

    @Override
    public StudyReminder getStudyReminderById(int studyReminderId) {
        return studyReminderRepository.findById(studyReminderId).orElseThrow(() -> new AppException(ErrorEnum.STUDY_REMINDER_NOT_FOUND));
    }
}
