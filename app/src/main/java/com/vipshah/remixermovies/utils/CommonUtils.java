package com.vipshah.remixermovies.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import com.squareup.moshi.Moshi;
import com.vipshah.remixermovies.models.RemixMovies;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public final class CommonUtils {

    private static final String[] MATERIAL_COLORS = {"#EC407A", "#8D6E63", "#5C6BC0", "#26A69A", "#66BB6A", "#BDBDBD"};

    private CommonUtils() {
        // hide constructor
    }

    @NonNull
    public static RemixMovies getMockMovies(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("movies.json");

            String movieJson = readFullyAsString(inputStream, StandardCharsets.UTF_8.name());
            return new Moshi.Builder().build().adapter(RemixMovies.class).fromJson(movieJson);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readFullyAsString(InputStream inputStream, String encoding)
            throws IOException {
        return readFully(inputStream).toString(encoding);
    }

    private static ByteArrayOutputStream readFully(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            stream.write(buffer, 0, length);
        }
        return stream;
    }

    public static float getPixels(Context context, int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static int getRandomColor() {
        String color = MATERIAL_COLORS[new Random().nextInt(MATERIAL_COLORS.length - 1)];
        return Color.parseColor(color);
    }
}
