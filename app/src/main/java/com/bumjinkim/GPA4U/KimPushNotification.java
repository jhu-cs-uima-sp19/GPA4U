package com.bumjinkim.GPA4U;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class KimPushNotification {
    public static void sendPush(final Context context) {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<KimCourse> courses = realm.where(KimCourse.class).findAll();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean allNotification = preferences.getBoolean("all_notifications", true);
        boolean gpaNotification = preferences.getBoolean("gpa_notifications", true);
        boolean expectedGPANotification = preferences.getBoolean("expected_gpa_notifications", true);

        if (allNotification) {
            if (gpaNotification) {
                new CountDownTimer(1000, 1000) {
                    public void onFinish() {
                        sendGPAPush(context, courses);
                    }

                    public void onTick(long millisUntilFinished) {
                    }
                }.start();
            }
            if (expectedGPANotification) {
                new CountDownTimer(3000, 1000) {
                    public void onFinish() {
                        sendExpectedGPAPush(context, courses);
                    }

                    public void onTick(long millisUntilFinished) {
                    }
                }.start();
            }
        }
    }

    private static void sendGPAPush(Context context, RealmResults<KimCourse> courses) {
        if (KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses)) < 3.00) {
            int KIM_NOTIFICATION_ID = randomInt();

            String id = random();
            String title = context.getString(R.string.kim_notification_channel_title);

            Intent intent;
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(id);

                if (notificationChannel == null) {
                    notificationChannel = new NotificationChannel(id, title, NotificationManager.IMPORTANCE_HIGH);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                    notificationManager.createNotificationChannel(notificationChannel);
                }

                builder = new NotificationCompat.Builder(context, id);
                intent = new Intent(context, KimMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, KIM_NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);

                builder.setContentTitle("Low GPA")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentText("Low GPA. Study until chair ripped off.")
                        .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                        .setAutoCancel(false)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setTicker("Low GPA. Study until chair ripped off.")
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            } else {
                builder = new NotificationCompat.Builder(context, id);
                intent = new Intent(context, KimMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                pendingIntent = PendingIntent.getActivity(context, KIM_NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);

                builder.setContentTitle("Low GPA")
                        .setContentText("Low GPA. Study until chair ripped off.")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                        .setAutoCancel(false)
                        .setContentIntent(pendingIntent)
                        .setTicker("Low GPA. Study until chair ripped off.")
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setPriority(NotificationCompat.PRIORITY_MAX);
            }
            Notification notification = builder.build();
            notificationManager.notify(KIM_NOTIFICATION_ID, notification);
        }
    }

    private static void sendExpectedGPAPush(Context context, RealmResults<KimCourse> courses) {
        if (KimCalculateGrade.calculateOverallExpectedGPA(new ArrayList<>(courses)) < 3.00) {
            int KIM_NOTIFICATION_ID = randomInt();

            String id = random();
            String title = context.getString(R.string.kim_notification_channel_title);

            Intent intent;
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(id);

                if (notificationChannel == null) {
                    notificationChannel = new NotificationChannel(id, title, NotificationManager.IMPORTANCE_HIGH);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                    notificationManager.createNotificationChannel(notificationChannel);
                }

                builder = new NotificationCompat.Builder(context, id);

                intent = new Intent(context, KimMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, KIM_NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);

                builder.setContentTitle("Low Expected GPA")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentText("Low Expected GPA. Study until chair ripped off.")
                        .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(false)
                        .setContentIntent(pendingIntent)
                        .setTicker("Low Expected GPA. Study until chair ripped off.")
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            } else {
                builder = new NotificationCompat.Builder(context, id);
                intent = new Intent(context, KimMainActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, KIM_NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);

                builder.setContentTitle("Low Expected GPA")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                        .setAutoCancel(false)
                        .setContentIntent(pendingIntent)
                        .setContentText("Low Expected GPA. Study until chair ripped off.")
                        .setTicker("Low Expected GPA. Study until chair ripped off.")
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setPriority(NotificationCompat.PRIORITY_MAX);
            }

            Notification notification = builder.build();
            notificationManager.notify(KIM_NOTIFICATION_ID, notification);
        }
    }

    public static int randomInt() {
        Random random = new Random();
        return random.nextInt(999999999 - 1) + 1;
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(100);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

}
