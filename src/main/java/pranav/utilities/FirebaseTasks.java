package pranav.utilities;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import static pranav.utilities.c.DATE_FORMAT;

public class FirebaseTasks {

    private final StorageReference appReference;
    @Nullable
    private UploadListener uploadListener;
    private UploadTask uploadTask;
    @Nullable
    private DownloadListener downloadListener;
    private String filePath;

    public FirebaseTasks() {
        appReference = FirebaseStorage.getInstance().getReference();
    }

    public void uploadFile(File file) {
        Uri tFile = Uri.fromFile(file);
        filePath = file.getParent() + tFile.getLastPathSegment();
        StorageReference fileReference = appReference.child(filePath);
        uploadTask = fileReference.putFile(tFile);
        uploadTask.addOnFailureListener(e -> {
            if (uploadListener != null) {
                uploadListener.onFailure(e);
            }
        }).addOnSuccessListener(taskSnapshot -> {
            if (uploadListener != null) {
                uploadListener.onSuccess(taskSnapshot);
            }
        }).addOnCompleteListener(task -> {
            if (uploadListener != null) {
                uploadListener.onComplete(task);
            }
        });
        uploadTask.addOnProgressListener(taskSnapshot -> {
            double progress = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
            if (uploadListener != null) {
                uploadListener.onProgress(progress);
            }
        });
    }

    public UploadTask getUploadTask() {
        return uploadTask;
    }

    public String getFilePath() {
        return filePath;
    }

    public void DownloadFileFromFireBase(@NonNull String pathName) {
        StorageReference fileReference = appReference.child(pathName);
        File file = null;
        try {
            file = File.createTempFile("image" + DATE_FORMAT.format(new Date()), ".jpg");
        } catch (IOException ex) {
            Logger.getLogger(FirebaseTasks.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (file != null) {
            //Todo:complete this?
            final File finalFile = file;
            fileReference.getFile(file).addOnSuccessListener(taskSnapshot -> {
                if (downloadListener != null) downloadListener.onSuccess(taskSnapshot);
            }).addOnFailureListener(exception -> {
                if (downloadListener != null) downloadListener.onFailure(exception);
            }).addOnProgressListener(taskSnapshot -> {
                int progress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                if (downloadListener != null) downloadListener.onProgress(progress);
            }).addOnCompleteListener(task -> {
                if (downloadListener != null) downloadListener.onComplete(task, finalFile);
            });
        }
    }

    public void setUploadListener(@Nullable UploadListener uploadListner) {
        this.uploadListener = uploadListner;
    }

    public void setDownloadListener(@Nullable DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public interface UploadListener {

        void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task);

        void onFailure(@NonNull Exception e);

        void onSuccess(@Nullable UploadTask.TaskSnapshot taskSnapshot);

        void onProgress(Double percentage);
    }

    public interface DownloadListener {

        void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot);

        void onFailure(@NonNull Exception exception);

        void onProgress(int progress);

        void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task, File file);
    }
}
