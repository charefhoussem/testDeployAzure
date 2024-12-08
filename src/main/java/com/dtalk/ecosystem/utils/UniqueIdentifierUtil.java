package com.dtalk.ecosystem.utils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.UUID;
import java.util.Base64;

public class UniqueIdentifierUtil {
    public static String generateSecureIdentifier(String originalFileName) throws NoSuchAlgorithmException {
        // Step 1: Extract file extension from the original file name
        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < originalFileName.length() - 1) {
            extension = originalFileName.substring(lastDotIndex + 1);
        }

        // Step 2: Generate a UUID
        String uuid = UUID.randomUUID().toString();

        // Step 3: Get the current timestamp
        String timestamp = String.valueOf(Instant.now().toEpochMilli());

        // Step 4: Combine UUID and timestamp
        String rawData = uuid + "_" + timestamp;

        // Step 5: Hash the raw data using SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));

        // Step 6: Encode the hash to a base64 string
        String secureIdentifier = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);

        // Step 7: Add the file extension if it exists
        return extension.isEmpty() ? secureIdentifier : secureIdentifier + "." + extension;
    }


}
