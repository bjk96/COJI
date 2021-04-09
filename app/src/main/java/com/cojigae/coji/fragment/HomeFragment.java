package com.cojigae.coji.fragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cojigae.coji.R;
import com.cojigae.coji.adapter.TodaySituationRecyclerViewAdapter;
import com.cojigae.coji.item.InfectionStateItem;
import com.cojigae.coji.item.TodaySituationItem;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

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

public class HomeFragment extends Fragment {
    Handler handler = new Handler();

    TextView tv_today;
    TextView textView;
    RecyclerView rv_today;
    ConstraintLayout layout;
    ConstraintLayout progressLayout;
    BarChart barChart;

    ArrayList<InfectionStateItem> infectionStateItemArrayList = new ArrayList<InfectionStateItem>();

    private final int CHART_MAX_COUNT = 6;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);

        tv_today = view.findViewById(R.id.tv_today);
        textView = view.findViewById(R.id.textView);
        rv_today = view.findViewById(R.id.rv_today);
        barChart = view.findViewById(R.id.decideGraph);
        layout = view.findViewById(R.id.layouth);
        progressLayout = view.findViewById(R.id.layout_progress);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rv_today.setLayoutManager(manager);

        TodayThread tThread = new TodayThread();
        tThread.start();

        return view;
    }

    private class TodayThread extends Thread {
        public void run() {
            Calendar calToday = Calendar.getInstance();         // 오늘 날짜
            Calendar calBefore = Calendar.getInstance();        // 열흘 전 날짜 계산하기 위해 선언
            calBefore.add(Calendar.DAY_OF_MONTH, -10);  // 열흘 전 날짜
            infectionStateItemArrayList.clear();


            String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson?serviceKey="
                    + serviceKey
                    + "&numOfRows=10&startCreateDt=" + sdf.format(calBefore.getTime())
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

                        infectionStateItemArrayList.add(new InfectionStateItem(todayDate, decide, exam, clear, death));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                barChart.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
                progressLayout.setVisibility(View.GONE);

                setDate();
                setMiddleBlock();
                setChart();
            });
        }
    }

    private class MyXAxisFormatter extends ValueFormatter{
        ArrayList<InfectionStateItem> arrayList;

        public MyXAxisFormatter(ArrayList<InfectionStateItem> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public String getFormattedValue(float value) {
            try {
                Date date = sdf.parse(arrayList.get((int) (CHART_MAX_COUNT-value)).getToday());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar.get(Calendar.MONTH)+1 + "/" + calendar.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
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

    private void setDate() {
        try {
            Date todayDate = sdf.parse(infectionStateItemArrayList.get(0).getToday());
            Calendar cal = Calendar.getInstance();
            cal.setTime(todayDate);

            tv_today.setText(cal.get(Calendar.YEAR) + "년 " + (cal.get(Calendar.MONTH)+1) + "월 " + cal.get(Calendar.DAY_OF_MONTH) + "일");

            cal.add(Calendar.DAY_OF_MONTH, -7);
            Log.i("YESTERDAY", sdf.format(cal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setMiddleBlock() {
        TodaySituationRecyclerViewAdapter adapter = new TodaySituationRecyclerViewAdapter(getContext());
        adapter.items.clear();

        int decideVal = Integer.parseInt(infectionStateItemArrayList.get(0).getDecide());
        int decideDiff = decideVal - Integer.parseInt(infectionStateItemArrayList.get(1).getDecide());
        int examVal = Integer.parseInt(infectionStateItemArrayList.get(0).getExam());
        int examDiff = examVal - Integer.parseInt(infectionStateItemArrayList.get(1).getExam());
        int clearVal = Integer.parseInt(infectionStateItemArrayList.get(0).getClear());
        int clearDiff = clearVal - Integer.parseInt(infectionStateItemArrayList.get(1).getClear());
        int deathVal = Integer.parseInt(infectionStateItemArrayList.get(0).getDeath());
        int deathDiff = deathVal - Integer.parseInt(infectionStateItemArrayList.get(1).getDeath());

        adapter.items.add(new TodaySituationItem("확진자", decideVal, decideDiff));
        adapter.items.add(new TodaySituationItem("검사중", examVal, examDiff));
        adapter.items.add(new TodaySituationItem("격리해제", clearVal, clearDiff));
        adapter.items.add(new TodaySituationItem("사망자", deathVal, deathDiff));

        rv_today.setAdapter(adapter);
    }

    private void setChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        int max = 0;

        for(int i = CHART_MAX_COUNT; i >= 0; i--){
            int difference = Integer.parseInt(infectionStateItemArrayList.get(i).getDecide()) - Integer.parseInt(infectionStateItemArrayList.get(i+1).getDecide());
            if(max < difference)
                max = difference;
            entries.add(new BarEntry(CHART_MAX_COUNT-i, difference));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "일별 확진자");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        barChart.setData(barData);

        barDataSet.setColor(Color.argb(0x88, 0xc0, 0, 0));

        barData.setBarWidth(0.2f);
        barData.setValueTextSize(13f);

        barChart.animateY(1500);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setTextSize(12f);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setValueFormatter(new MyXAxisFormatter(infectionStateItemArrayList));

        barChart.getAxisRight().setEnabled(false);

        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisLeft().setTextSize(12F);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMaximum(max+150);

        barChart.getLegend().setEnabled(false);

        int currentNightMode = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (currentNightMode){
            case Configuration.UI_MODE_NIGHT_YES:
                barData.setValueTextColor(Color.WHITE);
                barChart.getXAxis().setTextColor(Color.WHITE);
                barChart.getAxisLeft().setTextColor(Color.WHITE);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                barData.setValueTextColor(Color.BLACK);
                barChart.getXAxis().setTextColor(Color.BLACK);
                barChart.getAxisLeft().setTextColor(Color.BLACK);
                break;
        }
        barChart.invalidate();
    }
}