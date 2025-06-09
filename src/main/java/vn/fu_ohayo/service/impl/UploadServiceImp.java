package vn.fu_ohayo.service.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.common.collect.Lists;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.fu_ohayo.service.UploadService;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.List;

@Service
public class UploadServiceImp implements UploadService {

    private final ServletContext servletContext;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private static String CLIENT_KEY;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private static String SECRET_KEY;

    List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");

    private static final String APPLICATION_NAME = "FU-ohayo";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CHANEL_ID = "UChF07wZ-PnXnYsxJyRHd5dg";

    public UploadServiceImp(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public String handleUploadFile( MultipartFile file, String targetFolder) {
        // don't upload file
        if (file.isEmpty()) {
            return "";
        }
        // relative path: absolute path
        String rootPath = this.servletContext.getRealPath("/resources");
        String finalName = "";
        try {
            byte[] bytes = file.getBytes();

            File dir = new File(rootPath + File.separator + targetFolder);
            if (!dir.exists())
                dir.mkdirs();

            // Create the file on server
            finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);
            // uuid - monggoDB - 100 năm mới cso khả năng trùng
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalName;
    }

    @Override
    public String handleUploadFileToYoutube(MultipartFile file, String subjectName, String accessToken) throws GeneralSecurityException, IOException {
        //Khởi tạo YouTube service với accessToken
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        YouTube youtubeService = new YouTube.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                credential
        ).setApplicationName(APPLICATION_NAME).build();

        Video video = new Video();

        // add the snippet object property
        Calendar cal = Calendar.getInstance();
        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(subjectName);
        snippet.setChannelId(CHANEL_ID);
        snippet.setDescription("""
        This video is about the subject: %s
        Uploaded on: %s
        """.formatted(subjectName, cal.getTime()));

        //add status
        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus("public"); // or "private" or "unlisted"
        video.setStatus(status);

        video.setSnippet(snippet);

        // set the video file content

        InputStream inputStream = file.getInputStream();
        var mediaContent = new InputStreamContent(
                "video/*", inputStream
        );
        mediaContent.setLength(file.getSize());

        // upload the video

        YouTube.Videos.Insert request = youtubeService.videos().insert(List.of("snippet,status"), video, mediaContent);

        Video response = request.execute();

        return response.getId();
    }


}
