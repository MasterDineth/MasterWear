package model;

public class EmailBody {

    public static String getPasswordResetEmailBody(String username, String date, String time) {

        String body = "<body style=\"margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f5f5f5;\">\n"
                + "    <div style=\"max-width: 600px; margin: 0 auto; padding: 20px; background-color: #ffffff; border-radius: 0 0 15px 15px; \">\n"
                + "        <div style=\"text-align: center; padding: 20px;\">\n"
                + "            <h1 style=\"color: #333333; margin: 0; color: #2196F3;\">Master Wear</h1>\n"
                + "        </div>\n"
                + "        \n"
                + "        <div style=\"padding: 20px; border-top: 2px solid #eeeeee; border-bottom: 2px solid #eeeeee;\">\n"
                + "            <h2 style=\"color: #333333; margin-top: 0; text-align: center; padding-bottom: 10%; padding-top: 10px;\">Password Reset Confirmation</h2>\n"
                + "            <p style=\"color: #666666; line-height: 1.; padding-bottom: 10px;\">Dear <span style=\"font-weight: bolder;\">" + username + ",</span></p>\n"
                + "            <p style=\"color: #666666; line-height: 1.6;\">Your password has been successfully reset on<span style=\"background-color: #f8f8f8; border-radius: 4px; font-weight: bold; color: #333333;\"> " + date + "</span> at <span style=\"background-color: #f8f8f8; border-radius: 4px; font-weight: bold; color: #333333;\">" + time + "</span>.</p>\n"
                + "     \n"
                + "            <p style=\"color: #666666; line-height: 1.6;\">If you did not initiate this password reset, please reset password or contact us immediately at <a href=\"mailto:support@masterwear.com\" style=\"color: #007bff; text-decoration: none;\">support@masterwear.com</a>.</p>\n"
                + "        </div>\n"
                + "\n"
                + "        <div style=\"text-align: center; padding: 20px; color: #999999; font-size: 12px;\">\n"
                + "            <p>© 2025 Master Wear - Your Fashion Partner</p>\n"
                + "            <p style=\"margin: 5px 0;\">This is an automated message, please do not reply.</p>\n"
                + "        </div>\n"
                + "    </div>\n"
                + "</body>";

        return body;
    }
    
    public static String getVerificationCodeEmailBody(String vcode) {

        String body = "<body style=\"font-family: 'Segoe UI', Arial, sans-serif; line-height: 1.6; margin: 0; padding: 0; background-color: #f5f5f5;\">\n"
                                + "    <div class=\"container\" style=\"max-width: 600px; margin: 40px auto; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);\">\n"
                                + "        <div class=\"logo\" style=\"text-align: center; margin-bottom: 30px;\">\n"
                                + "            <h1 style=\"color: #2196F3; margin: 0; font-size: 28px;\">Smart Wear</h1>\n"
                                + "        </div>\n"
                                + "        <div class=\"content\" style=\"padding: 20px;\">\n"
                                + "            <h2>Verify Your Account</h2>\n"
                                + "            <p>Thank you for creating an account with Smart Wear. To complete your registration, please use the verification code below:</p>\n"
                                + "            \n"
                                + "            <div class=\"verification-code\" style=\"background-color: #E3F2FD; padding: 15px; border-radius: 4px; text-align: center; font-size: 24px; letter-spacing: 2px; margin: 20px 0; color: #1976D2;\">\n"
                                + "                " + vcode + "\n"
                                + "            </div>\n"
                                + "\n"
                                + "            <p>Enter this code on the verification page to activate your account. This code will expire in 10 minutes.</p>\n"
                                + "            \n"
                                + "            <p>If you didn't create an account with Smart Wear, please ignore this email.</p>\n"
                                + "        </div>\n"
                                + "        <div class=\"footer\" style=\"text-align: center; color: #757575; font-size: 12px; margin-top: 30px;\">\n"
                                + "            <p>© 2025 Smart Wear. All rights reserved.</p>\n"
                                + "            <p>This is an automated message, please do not reply to this email.</p>\n"
                                + "        </div>\n"
                                + "    </div>\n"
                                + "</body>";

        return body;
    }

}
