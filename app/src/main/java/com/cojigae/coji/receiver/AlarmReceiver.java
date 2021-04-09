package com.cojigae.coji.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.cojigae.coji.MainActivity;
import com.cojigae.coji.R;
import com.cojigae.coji.item.InfectionStateItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {

    Handler handler = new Handler();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.ic_launcher_foreground);

            String channelName ="매일 알람 채널";
            String description = "매일 정해진 시간에 알람합니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }else builder.setSmallIcon(R.mipmap.ic_launcher);

        NotificationThread thread = new NotificationThread(builder, pendingIntent, notificationManager, context);
        thread.start();
    }

    private class NotificationThread extends Thread {
        NotificationCompat.Builder builder;
        PendingIntent pendingIntent;
        NotificationManager notificationManager;
        Context mContext;

        public NotificationThread(NotificationCompat.Builder builder, PendingIntent pendingIntent, NotificationManager notificationManager, Context mContext){
            this.builder = builder;
            this.pendingIntent = pendingIntent;
            this.notificationManager = notificationManager;
            this.mContext = mContext;
        }

        public void run(){
            Calendar calToday = Calendar.getInstance();
            Calendar calBefore = Calendar.getInstance();
            calBefore.add(Calendar.DAY_OF_MONTH, -3);

            ArrayList<InfectionStateItem> items = new ArrayList<>();

            String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson?serviceKey="
                    + serviceKey
                    + "&startCreateDt=" + sdf.format(calBefore.getTime())
                    + "&endCreateDt=" + sdf.format(calToday.getTime());

            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(url);
                doc.getDocumentElement().normalize();
                NodeList nodes = doc.getElementsByTagName("item");
                Log.i("value", "nodes size = " + nodes.getLength());

                for(int i = 0; i < nodes.getLength(); i++){
                    Node node = nodes.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String todayDate = getTagValue("stateDt", element);
                        String decide = getTagValue("decideCnt", element);
                        String exam = getTagValue("examCnt", element);
                        String clear = getTagValue("clearCnt", element);
                        String death = getTagValue("deathCnt", element);

                        items.add(new InfectionStateItem(todayDate, decide, exam, clear, death));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    Date todayDate;
                    try {
                        todayDate = sdf.parse(items.get(0).getToday());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(todayDate);

                        builder.setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setContentTitle(cal.get(Calendar.YEAR) + "년 " + (cal.get(Calendar.MONTH) + 1) + "월 " + cal.get(Calendar.DAY_OF_MONTH) + "일 코로나19 감염현황")
                                .setContentText("총 확진자 : " + items.get(0).getDecide() + "명, 오늘 확진자 : " + (Integer.parseInt(items.get(0).getDecide()) - Integer.parseInt(items.get(1).getDecide())) + "명")
                                .setContentIntent(pendingIntent)
                                .setColor(Color.rgb(0, 0x70, 0x90))
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        if (notificationManager != null) {
                            notificationManager.notify(1234, builder.build());
                            Calendar nextNotifyTime = Calendar.getInstance();
                            nextNotifyTime.add(Calendar.DATE, 1);

                            SharedPreferences.Editor editor = mContext.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                            editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
                            editor.apply();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private String getTagValue(String tag, Element element) {
        try{
            NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node value = nodes.item(0);
            return value.getNodeValue();
        } catch(NullPointerException | IllegalStateException e){
            return null;
        }
    }
}