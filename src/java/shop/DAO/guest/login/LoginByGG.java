/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.guest.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import shop.model.AccountGG;

public class LoginByGG {

    public String getToken(String code) throws IOException, InterruptedException {
        // Debug: In mã code để kiểm tra có hợp lệ không
        System.out.println("Authorization Code: " + code);

        // Kiểm tra nếu mã code bị trống hoặc null
        if (code == null || code.isEmpty()) {
            throw new IOException("Authorization code is null or empty.");
        }

        // Tạo request body dạng x-www-form-urlencoded
        String requestBody = "client_id=" + URLEncoder.encode(Iconstant.GOOGLE_CLIENT_ID, StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(Iconstant.GOOGLE_CLIENT_SECRET, StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(Iconstant.GOOGLE_REDIRECT_URI, StandardCharsets.UTF_8)
                //Do GG cấp
                + "&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                //Chỉ định kiểu grant OAuth đang sử dụng
                + "&grant_type=authorization_code";  // Không cần encode giá trị này

        // Gửi yêu cầu HTTP POST tới GG
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Iconstant.GOOGLE_LINK_GET_TOKEN))
                .header("Content-Type", "application/x-www-form-urlencoded")
                // Body request: Chứa thông tin ứng dụng và authorization code
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        //Nhận phản hồi từ GG
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Debug --> In phản hồi từ GG
        System.out.println("Response: " + response.body());

        //status code 200 là kết nối thành công
        if (response.statusCode() != 200) {
            throw new IOException("Failed to get token: " + response.body());
        }

        JsonObject jobj = new Gson().fromJson(response.body(), JsonObject.class);
        return jobj.get("access_token").getAsString();
    }

    public AccountGG getUserInfo(String accessToken) throws IOException, InterruptedException {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IOException("Access token is null or empty.");
        }

        String link = Iconstant.GOOGLE_LINK_GET_USER_INFO + "?access_token=" + accessToken;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(link))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("User Info Response: " + response.body());

        if (response.statusCode() != 200) {
            throw new IOException("Failed to get user info: " + response.body());
        }

        return new Gson().fromJson(response.body(), AccountGG.class);
    }
}
