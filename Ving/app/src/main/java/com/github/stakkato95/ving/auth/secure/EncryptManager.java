package com.github.stakkato95.ving.auth.secure;

import android.content.Context;
import android.provider.Settings;
import android.util.Base64;
import android.widget.Toast;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by Artyom on 30.11.2014.
 */
public class EncryptManager {
    public static final String ENCODING = "UTF8";
    public static final String DES = "DES";


    public static String encrypt(final Context context, final String value) throws Exception {
        String sourceKey = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        //Toast.makeText(context, sourceKey, Toast.LENGTH_SHORT).show();
        byte[] bytesKey = sourceKey.getBytes(ENCODING);
        final DESKeySpec keySpec = new DESKeySpec(bytesKey);
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        final SecretKey key = keyFactory.generateSecret(keySpec);
        final byte[] text = value.getBytes(ENCODING);
        final Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.encodeToString(cipher.doFinal(text), Base64.DEFAULT);

    }

    public static String decrypt(final Context context, final String value) throws Exception {
        String sourceKey = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        //Toast.makeText(context, sourceKey, Toast.LENGTH_SHORT).show();
        byte[] bytesKey = sourceKey.getBytes(ENCODING);
        final DESKeySpec keySpec = new DESKeySpec(bytesKey);
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        final SecretKey key = keyFactory.generateSecret(keySpec);
        final byte[] encrypedBytes = Base64.decode(value, Base64.DEFAULT);
        final Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.DECRYPT_MODE, key);
        final byte[] plainTextBytes = cipher.doFinal(encrypedBytes);
        return new String(plainTextBytes);
    }
}
