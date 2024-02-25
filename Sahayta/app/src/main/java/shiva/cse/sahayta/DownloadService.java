package shiva.cse.sahayta;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadService extends IntentService {
    private static final String CHANNEL_ID = "download_service_channel";
    private static final int NOTIFICATION_ID = 1;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        createNotificationChannel();
        FirebaseDatabase.getInstance().getReference("Complaints").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder dataContent = new StringBuilder();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming your data structure includes fields like Name, Type, Desc
                    String name = snapshot.child("Name").getValue(String.class);
                    String type = snapshot.child("Type").getValue(String.class);
                    String desc = snapshot.child("Desc").getValue(String.class);
                    dataContent.append("Name: ").append(name).append("\nType: ").append(type).append("\nDescription: ").append(desc).append("\n\n");
                }
                createPdf(dataContent.toString());
                showDownloadCompleteNotification();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void createPdf(String content) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        page.getCanvas().drawText(content, 10, 10, new Paint());

        document.finishPage(page);

        File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "MyPDFs");
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfFile = new File(pdfDir, "complaint.pdf");
        try {
            document.writeTo(new FileOutputStream(pdfFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Download Service";
            String description = "Channel for Download Service";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showDownloadCompleteNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Download Complete")
                .setContentText("Your PDF has been downloaded.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}