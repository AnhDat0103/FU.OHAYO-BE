package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.AnswerQuestionRequest;
import vn.fu_ohayo.dto.request.UserQuestionResponseRequest;
import vn.fu_ohayo.dto.request.UserResponseRequest;
import vn.fu_ohayo.dto.response.*;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ExerciseQuestionMapper;
import vn.fu_ohayo.mapper.LessonExerciseMapper;
import vn.fu_ohayo.repository.*;
import vn.fu_ohayo.service.ProgressExerciseService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgressExerciseServiceImp implements ProgressExerciseService {

    private final UserResponseQuestionRepository userResponseQuestionRepository;
    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final UserRepository userRepository;
    private final ExerciseQuestionMapper exerciseQuestionMapper;
    private final LessonExerciseRepository lessonExerciseRepository;
    private final AnswerQuestionRepository answerQuestionRepository;
    private final LessonExerciseMapper lessonExerciseMapper;
    public ProgressExerciseServiceImp(UserResponseQuestionRepository userResponseQuestionRepository,
                                      ExerciseQuestionRepository exerciseQuestionRepository,
                                      UserRepository userRepository,
                                      ExerciseQuestionMapper exerciseQuestionMapper,
                                      LessonExerciseRepository lessonExerciseRepository,
                                      AnswerQuestionRepository answerQuestionRepository,
                                      LessonExerciseMapper lessonExerciseMapper
                                     )  {
        this.userResponseQuestionRepository = userResponseQuestionRepository;
        this.exerciseQuestionRepository = exerciseQuestionRepository;
        this.userRepository = userRepository;
        this.exerciseQuestionMapper = exerciseQuestionMapper;
        this.lessonExerciseRepository = lessonExerciseRepository;
        this.answerQuestionRepository = answerQuestionRepository;
        this.lessonExerciseMapper = lessonExerciseMapper;
    }


    @Override
    public LessonExerciseResponse getSource(int exerciseId, int lessonId) {
        LessonExercise lessonExercise = lessonExerciseRepository.findByLesson_LessonId(lessonId).orElseThrow(
                () -> new AppException(ErrorEnum.EXERCISE_NOT_FOUND)
        );

        List<ExerciseQuestionResponse> questions = exerciseQuestionRepository.findAllByLessonExercise(lessonExercise).stream()
                .map(exerciseQuestionMapper::toExerciseQuestionResponse).toList();
        return LessonExerciseResponse.builder()
                .lessonId(lessonId)
                .title(lessonExercise.getTitle())
                .duration(lessonExercise.getDuration())
                .content(questions)
                .id(exerciseId)
                .build();
    }


    @Override
    public ExerciseResultResponse submitExercise(UserResponseRequest userResponseRequest) {
        User user = userRepository.findById(userResponseRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        LessonExercise lessonExercise = lessonExerciseRepository.findById(userResponseRequest.getExerciseId())
                .orElseThrow(() -> new AppException(ErrorEnum.EXERCISE_NOT_FOUND));


        List<ExerciseQuestion> exerciseQuestions = exerciseQuestionRepository.findAllByLessonExercise(lessonExercise);

        List<ExerciseQuestionResponse> exerciseQuestionResponse = exerciseQuestions.stream().map(
                exerciseQuestionMapper::toExerciseQuestionResponse
        ).toList();

        List<QuestionResultResponse> questionResultResponse = getQuestionResultResponses(exerciseQuestionResponse, userResponseRequest.getUserQuestionResponseRequests());

        ExerciseResultResponse response = ExerciseResultResponse.builder()
                .exerciseId(lessonExercise.getExerciseId())
                .totalQuestion(exerciseQuestions.size())
                .questionResultResponses(questionResultResponse)
                .totalCorrect((int) questionResultResponse.stream()
                        .filter(QuestionResultResponse::isCorrect)
                        .count())
                .totalTime(userResponseRequest.getTotalTime())
                .userId(user.getUserId())
                .build();

        saveUserResponses(userResponseRequest, exerciseQuestionResponse, user);

         return  response;
    }


    public List<QuestionResultResponse> getQuestionResultResponses(
            List<ExerciseQuestionResponse> exerciseQuestionResponses,
            List<UserQuestionResponseRequest> userQuestionResponseRequests) {
        List<QuestionResultResponse> questionResultResponses = new ArrayList<>();
        exerciseQuestionResponses.forEach(e-> {
            userQuestionResponseRequests.forEach(u -> {
                if(e.getExerciseQuestionId() == u.getQuestionId()){
                    QuestionResultResponse questionResultResponse = QuestionResultResponse.builder()
                            .isCorrect(checkCorrectAnswer(e,u))
                            .userResponseId(u.getSelectedAnswerId())
                            .exerciseQuestionResponse(e)
                            .build();
                    questionResultResponses.add(questionResultResponse);
                }
            });
        });
        return  questionResultResponses;

    }

    public boolean checkCorrectAnswer(ExerciseQuestionResponse e, UserQuestionResponseRequest u) {
        if(u.getSelectedAnswerId() == -1) {
            return false;
        }
        return e.getAnswerQuestions().stream()
                .anyMatch(
                        t -> t.getAnswerId() == u.getSelectedAnswerId()
                        && t.getIsCorrect() == true
                );
    }

    public void saveUserResponses(
            UserResponseRequest userResponseRequest,
            List<ExerciseQuestionResponse> exerciseQuestionResponses,
            User user
    ) {
        List<UserResponseQuestion> userResponseQuestions = new ArrayList<>();
        for (ExerciseQuestionResponse e : exerciseQuestionResponses) {
            for(UserQuestionResponseRequest u : userResponseRequest.getUserQuestionResponseRequests()) {
                if(e.getExerciseQuestionId() == u.getQuestionId()) {
                    UserResponseQuestion userResponseQuestion = UserResponseQuestion.builder()
                            .question(exerciseQuestionRepository.findById(u.getQuestionId()).get())
                            .user(user)
                            .answerQuestion(answerQuestionRepository.findById(u.getSelectedAnswerId()).get())
                            .isCorrect(checkCorrectAnswer(e,u))
                            .build();
                    userResponseQuestions.add(userResponseQuestion);
                }
            }


        }
        userResponseQuestionRepository.saveAll(userResponseQuestions);
    }

}
