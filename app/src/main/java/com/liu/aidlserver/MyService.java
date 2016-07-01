package com.liu.aidlserver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @PackageName: com.liu.aidlserver
 * @Description: 服务端
 * @author: LanYing
 * @date: 2016/6/30 11:52
 */
public class MyService extends Service {

    private final static String TAG = "MyService";
    private boolean canRun = true;
    private List<Student> mStudents = new ArrayList<>();
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        Thread thread = new Thread(null, runnable, "BrankGroundService");
        thread.start();

        synchronized (mStudents){
            for (int i = 1; i < 6; i++){
                Student student = new Student();
                student.name = "student#" + i;
                student.age = i * 5;
                mStudents.add(student);
            }
        }
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        super.onCreate();
    }

    private final IMyService.Stub mBinder = new IMyService.Stub() {
        @Override
        public List<Student> getStudent() throws RemoteException {
            synchronized (mStudents) {
                return mStudents;
            }
        }

        @Override
        public void addStudent(Student student) throws RemoteException {
            synchronized (mStudents) {
                if (!mStudents.contains(student)) {
                    mStudents.add(student);
                }
            }
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        canRun = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, String.format("on bind,intent = %s", intent.toString()));
        displayNotificationMessage("服务已启动");
        return mBinder;
    }

    private void displayNotificationMessage(String message) {

        PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this, MainActivity.class), 0);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis()).setAutoCancel(false)
                .setContentTitle("hah").setContentText("dfdf");
        startForeground(R.mipmap.ic_launcher, builder.getNotification());
    }

    Runnable runnable = new Runnable() {
        long counter = 0;
        @Override
        public void run() {
            while (canRun){
                // do background processing here.....
                Log.d("scott",""+counter);
                counter++;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
