package com.cojigae.coji.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cojigae.coji.adapter.KorSituationAdapter;
import com.cojigae.coji.R;
import com.cojigae.coji.item.CoronaSituationKR;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class KoreaSituationFragment extends Fragment {
    String date;

    List<CoronaSituationKR> coronaSituationKRList = new ArrayList<>();
    Handler handler = new Handler();

    TextView today_text;

    RecyclerView recyclerView;
    KorSituationAdapter adapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    LinearLayout layout_content;
    ConstraintLayout layout_progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_korea_situation, container, false);

        today_text = view.findViewById(R.id.today_text);

        layout_content = view.findViewById(R.id.layout_kor_contents);
        layout_progress = view.findViewById(R.id.layout_progress);

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new KorSituationAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());

        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);

        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        KorSituationAdapter adapter = new KorSituationAdapter();
        recyclerView.setAdapter(adapter);

        dataList();
        return view;
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

    private void dataList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Calendar calToday = Calendar.getInstance();
                    Calendar calYesterday = Calendar.getInstance();
                    calYesterday.add(Calendar.DAY_OF_MONTH, -3);
                    coronaSituationKRList.clear();


                    String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=" +
                            serviceKey + "&numOfRows=10&startCreateDt=" + dateFormat.format(calYesterday.getTime())
                            + "&endCreateDt=" + dateFormat.format(calToday.getTime());
                    Log.i("url", url);

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
                            String guBun = getTagValue("gubun", element);
                            int defCnt = Integer.parseInt(getTagValue("defCnt", element));
                            int deathCnt = Integer.parseInt(getTagValue("deathCnt", element));
                            int incDec = Integer.parseInt(getTagValue("incDec", element));
                            String stdDay = getTagValue("stdDay", element);

                            adapter.items.add(new CoronaSituationKR(guBun, defCnt, deathCnt, incDec, stdDay));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);

                        layout_content.setVisibility(View.VISIBLE);
                        layout_progress.setVisibility(View.GONE);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 hh시");
                        Calendar calendar = Calendar.getInstance();
                        try {
                            Date date = sdf.parse(adapter.items.get(0).getStdDay());
                            calendar.setTime(date);
                            today_text.setText(calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH)+1) + "월 " + calendar.get(Calendar.DAY_OF_MONTH) + "일");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
}