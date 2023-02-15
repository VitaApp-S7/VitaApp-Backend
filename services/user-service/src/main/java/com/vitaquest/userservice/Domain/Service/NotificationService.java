package com.vitaquest.userservice.Domain.Service;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.vitaquest.userservice.Domain.DTO.Notification.AllUserNotificationDTO;
import com.vitaquest.userservice.Domain.DTO.Notification.MessageToUserDTO;
import com.vitaquest.userservice.Domain.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class NotificationService {

    private final UserService userService;

    @Autowired
    public NotificationService(UserService userService) {
        this.userService = userService;
    }

    private void SendNotification(String tokenInput, String titleInput, String messageInput) throws Exception {
        JSONObject data = new JSONObject();
        data.put("to", "ExponentPushToken[" + tokenInput + "]");
        data.put("title", titleInput);
        data.put("body", messageInput);

        URL url = new URL("https://exp.host/--/api/v2/push/send");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        os.write(data.toString().getBytes("UTF-8"));
        os.close();

        System.out.println("Response code: " + con.getResponseCode());
    }

    public void HandleAllUserNotifications(AllUserNotificationDTO dto) throws Exception {
        List<User> users = userService.getAllUsers();
        String title = dto.getTitle();
        String message = dto.getMessage();
        for (User user : users) {
            String token = user.getExpoToken();
            if (token != null) {
                SendNotification(token, title, message);
            }
        }
    }

    public void HandleUserTestNotification(Authentication authContext) throws Exception {
        User user = userService.getUserInfo(authContext);
        if (user.getExpoToken() != null)
            SendNotification(user.getExpoToken(), "Test Notification", "This is your test Notification");
    }

    public void HandleMessageToGivenUser(String token, String title, String message) throws Exception{
        SendNotification(token, title, message);
    }

    public void HandleToUserNotifications(MessageToUserDTO dto) throws Exception {
        SendNotification(
                userService.getUserById(dto.getReceiver()).getExpoToken(),
                dto.getTitle(),
                dto.getMessage()
        );
    }
}
