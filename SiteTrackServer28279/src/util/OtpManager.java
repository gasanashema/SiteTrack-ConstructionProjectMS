package util;

import dao.OtpVerificationDao;
import model.OtpVerification;
import model.User;
import java.security.SecureRandom;
import java.time.LocalDateTime;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OtpManager {

    private static OtpManager instance;
    private final OtpVerificationDao otpDao;
    private final SecureRandom secureRandom;
    
    // Rate Limiting Fields
    private static final int MAX_OTP_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 15;
    private final Map<String, OtpAttempt> attemptTracker;

    private static class OtpAttempt {
        int failedAttempts;
        LocalDateTime lastAttemptTime;
        boolean isLockedOut;
    }

    private OtpManager() {
        this.otpDao = new OtpVerificationDao();
        this.secureRandom = new SecureRandom();
        this.attemptTracker = new ConcurrentHashMap<>();
    }

    public static synchronized OtpManager getInstance() {
        if (instance == null) {
            instance = new OtpManager();
        }
        return instance;
    }

    // --- Rate Limiting Methods ---
    
    public boolean isUserLockedOut(String userId) {
        OtpAttempt attempt = attemptTracker.get(userId);
        if (attempt == null) return false;
        
        if (attempt.isLockedOut) {
            LocalDateTime lockoutExpiry = attempt.lastAttemptTime.plusMinutes(LOCKOUT_DURATION_MINUTES);
            if (LocalDateTime.now().isBefore(lockoutExpiry)) {
                return true; // Still locked out
            } else {
                attempt.failedAttempts = 0;
                attempt.isLockedOut = false;
                return false; // Lockout expired
            }
        }
        return false;
    }

    public void recordFailedOtpAttempt(String userId) {
        OtpAttempt attempt = attemptTracker.computeIfAbsent(userId, k -> new OtpAttempt());
        attempt.failedAttempts++;
        attempt.lastAttemptTime = LocalDateTime.now();
        
        if (attempt.failedAttempts >= MAX_OTP_ATTEMPTS) {
            attempt.isLockedOut = true;
            System.err.println("SECURITY: User " + userId + " locked out due to failed OTP attempts");
        }
    }

    public void resetOtpAttempts(String userId) {
        OtpAttempt attempt = attemptTracker.get(userId);
        if (attempt != null) {
            attempt.failedAttempts = 0;
            attempt.isLockedOut = false;
        }
    }

    public boolean createAndSendOtp(User user) {
        try {
            // 1. Invalidate any previously unused OTPs for this user
            otpDao.markAllUsedForUser(user.getId());

            // 2. Generate new OTP code
            String otpCode = generateOtpCode();

            // 3. Determine expiration
            int expiryMinutes = 5;
            try {
                String configuredExpiry = ConfigManager.getInstance().getProperty("otp.expiry.minutes", "5");
                expiryMinutes = Integer.parseInt(configuredExpiry);
            } catch (NumberFormatException e) {
                // Ignore and use default 5
            }

            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(expiryMinutes);

            // 4. Save to Database
            OtpVerification otpRecord = new OtpVerification();
            otpRecord.setUser(user);
            otpRecord.setOtpCode(otpCode);
            otpRecord.setExpiresAt(expiresAt);
            otpRecord.setUsed(false);
            otpRecord.setCreatedAt(LocalDateTime.now());

            otpDao.save(otpRecord);

            // 5. Send via Email
            boolean emailSent = EmailService.sendOtpEmail(user.getEmail(), otpCode);
            
            if (emailSent) {
                System.out.println("OTP successfully generated and sent to " + user.getEmail());
                return true;
            } else {
                System.err.println("OTP generated but failed to send to " + user.getEmail());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateOtpCode() {
        // Generate a 6 digit code cryptographically securely
        int code = secureRandom.nextInt(1000000);
        return String.format("%06d", code);
    }
}
