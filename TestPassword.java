import com.gov.common.utils.PasswordUtils;

public class TestPassword {
    public static void main(String[] args) {
        String password = "123456";
        String encryptedPassword = PasswordUtils.encrypt(password);
        System.out.println("原始密码: " + password);
        System.out.println("加密后密码: " + encryptedPassword);
        System.out.println("加密后密码长度: " + encryptedPassword.length());
        System.out.println("密码验证结果: " + PasswordUtils.matches(password, encryptedPassword));
    }
}