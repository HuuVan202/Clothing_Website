/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
function togglePassword(inputId, icon) {
    let passwordField = document.getElementById(inputId);
    if (passwordField.type === "password") {
        passwordField.type = "text";
        icon.textContent = "🙈"; // Biểu tượng ẩn mật khẩu
    } else {
        passwordField.type = "password";
        icon.textContent = "👁️"; // Biểu tượng hiển thị mật khẩu
    }
}


