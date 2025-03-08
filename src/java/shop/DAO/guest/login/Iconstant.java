/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.guest.login;

public class Iconstant {

    public static final String GOOGLE_CLIENT_ID = "6921099833-mssavtev5dthku986lu1fun9d18b6tcn.apps.googleusercontent.com";
    public static final String GOOGLE_CLIENT_SECRET = "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";

    // Sử dụng đúng redirect_uri như khi lấy mã code
    public static final String GOOGLE_REDIRECT_URI = "http://localhost:8080/ClothingShop/LoginByGoogle";

    public static final String GOOGLE_GRANT_TYPE = "authorization_code";

    // Address endpoint của Google dùng để lấy Access Token.
    public static final String GOOGLE_LINK_GET_TOKEN = "https://oauth2.googleapis.com/token";
    // Sau khi lấy được access token --> gọi đến API này để get user
    public static final String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo";
}
