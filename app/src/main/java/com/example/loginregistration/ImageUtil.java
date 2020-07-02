package com.example.loginregistration;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtil {
    public static Bitmap convert(String base64Str) throws IllegalArgumentException {
        if (base64Str != null) {
            byte[] decodedBytes = Base64.decode(
                    base64Str.substring(base64Str.indexOf(",") + 1),
                    Base64.DEFAULT
            );

            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } else {
            String android = "iVBORw0KGgoAAAANSUhEUgAAAGAAAABgCAQAAABIkb+zAAACSElEQVR4Ae2aT0tUYRSHHwhyXAkF" +
                    "/SElCCToS9QmcGvMpihq0z9CzMFN0FeIoPoMbQKjbSBtBxwFZWgT5TIYoqHxasLIPbkYwmQuB+6957wE53m274XfAzKLg5SmSY" +
                    "/vzFOVe/TY5iru/EAQhjSpwgNyBGELd3pI5YSHo/nCJu7MM6yY8Ojv/D2ukYBmQcI0d3jDJ7bJyMnJ+MYqr7jFuYL51" +
                    "/FCSTjFUzpIoTltnjAFPPafrye8ZRfRZcA7//lKQgmV+Z48Kx1wn/TQZLd0QJ85ErNAjlRwyN2086WyebqEG+RIDR" +
                    "4wRwKukCE12ecSzpxkE6nRNidw5TlSsy0cOT/mp5MRolj0bsAZ3HiN1B4gvMCJ0/w2CciYwoUWYhIgLODCOmJkGwcuIGbmnMWc24ihN11" +
                    "/gQx8iTmrpgEfMeeracAXzBmYBvzCnAPTgCFBENTAJMtssH/oOi0aHEWMVXfoXOQzcsQuM44Byg6dyX8" +
                    "/G3064Reg7FBZRsa45Beg7FDZQMbY8QtQdqjsF9yR3QKUHSpSoF+AsiMCDIyACIiACIiACIiACIiACIiACIiACIiACIiACIiA" +
                    "/K4q/8zzZpvgLKjxL8zLfoFKDtUGnSRY24x4Rig7NCZoXvss2lwDVB26DRYosPeoWssMgH+AeqO0mSIoTuY88E0YAVzLtNHjPzJ" +
                    "LHgkvGfH4I9nhVkSIAWWfacTAREQAREQAREQAREQARGg32/KvXNHv9/o75Ki32/0d0nR7zf6u8To9xv9XWL0+43ZnecPKogex63Vp08AAAAASUVORK5CYII=";
            byte[] decodedBytes1 = Base64.decode(
                    android.substring(android.indexOf(",") + 1),
                    Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes1, 0, decodedBytes1.length);
        }


    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

}
