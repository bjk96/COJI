package com.cojigae.coji.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cojigae.coji.R;
import com.cojigae.coji.adapter.WorldSituationAdapter;
import com.cojigae.coji.item.WorldSituationItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class WorldSituationFragment extends Fragment {

    Handler handler = new Handler();

    private RecyclerView recyclerView;
    private WorldSituationAdapter adapter;

    private ConstraintLayout layout_content;
    private ConstraintLayout layout_progress;
    public Spinner spinner;
    private TextView tv_date;
    ArrayList<WorldSituationItem> allItems;
    ArrayList<WorldSituationItem> allItems2;
    ArrayList<WorldSituationItem> searchItems;
    ArrayList<WorldSituationItem> searchItems2;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_world_situation, container, false);
        recyclerView = root.findViewById(R.id.RecyclerView);

        layout_content = root.findViewById(R.id.layout_world);
        layout_progress = root.findViewById(R.id.layout_world_progress);
        spinner = root.findViewById(R.id.Spinner);
        tv_date = root.findViewById(R.id.tv_date);
        allItems = new ArrayList<>();
        allItems2 = new ArrayList<>();
        searchItems = new ArrayList<>();
        searchItems2 = new ArrayList<>();

        String[] area = getActivity().getResources().getStringArray(R.array.continent);

        adapter = new WorldSituationAdapter();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);

        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));

        MyThread thread = new MyThread();
        thread.start();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        adapter.items = allItems;
                        adapter.items2 = allItems2;
                        adapter.notifyDataSetChanged();
                        break;
                    case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                        invalidateItems(area[position]);
                        adapter.notifyDataSetChanged();
                        break;
                }
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return root;
    }

    private void invalidateItems(String area) {
        searchItems.clear();
        searchItems2.clear();
        for(int i = 0; i < allItems.size(); i++) {
            if (allItems.get(i).getAreaNm().equals(area) && allItems2.get(i).getAreaNm().equals(area)){
                // 김병주 210118
                if(allItems.get(i).getNationNm().equals(allItems2.get(i).getNationNm())) {
                    searchItems.add(allItems.get(i));
                    searchItems2.add(allItems2.get(i));
                } else if(allItems.get(i).getNationNm().equals(allItems2.get(i+(allItems.size()-allItems2.size())).getNationNm())) {
                    searchItems.add(allItems.get(i));
                    searchItems2.add(allItems2.get(i+(allItems.size()-allItems2.size())));
                }
            }
        }
        adapter.items = searchItems;
        adapter.items2 = searchItems2;
        adapter.notifyDataSetChanged();
    }

    private class MyThread extends Thread {

        Calendar calToday = Calendar.getInstance();
        Calendar calBefore = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();

        public void run() {
            calBefore.add(Calendar.DAY_OF_MONTH, -3);

            String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19NatInfStateJson?serviceKey="
                    + serviceKey
                    + "&numOfRows=10&startCreateDt=" + sdf.format(calBefore.getTime())
                    + "&endCreateDt=" + sdf.format(calToday.getTime());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(url);
                document.getDocumentElement().normalize();

                NodeList nodeList = document.getElementsByTagName("item");
                Log.i("Main", "노드리스트의 갯수=" + nodeList.getLength());

                yesterday.add(Calendar.DAY_OF_MONTH, -1);

                for(int i = 0; i < nodeList.getLength(); i++){
                    Node node = nodeList.item(i);

                    if(node.getNodeType() == Node.ELEMENT_NODE){
                        Element element = (Element)node;

                        String nationNm = getTagValue("nationNm", element);
                        String natDefCnt = getTagValue("natDefCnt", element);
                        String natDeathCnt = getTagValue("natDeathCnt", element);
                        String areaNm = getTagValue("areaNm", element);
                        String stdDay = getTagValue("stdDay", element);

                        Calendar calNode = Calendar.getInstance();
                        SimpleDateFormat sdfNode = new SimpleDateFormat("yyyy년 MM월 dd일 hh시");
                        Date date = sdfNode.parse(stdDay);
                        calNode.setTime(date);

                        if(i == 0 && calNode.get(Calendar.DAY_OF_MONTH) != calToday.get(Calendar.DAY_OF_MONTH)) {
                            calToday.add(Calendar.DAY_OF_MONTH, -1);
                            yesterday.add(Calendar.DAY_OF_MONTH, -1);
                        }

                        if(calNode.get(Calendar.YEAR) == calToday.get(Calendar.YEAR) && calNode.get(Calendar.MONTH) == calToday.get(Calendar.MONTH) && calNode.get(Calendar.DAY_OF_MONTH) == calToday.get(Calendar.DAY_OF_MONTH))
                            allItems.add(new WorldSituationItem(nationNm, natDefCnt, natDeathCnt, areaNm, stdDay));
                        else if(calNode.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calNode.get(Calendar.MONTH) == yesterday.get(Calendar.MONTH) && calNode.get(Calendar.DAY_OF_MONTH) == yesterday.get(Calendar.DAY_OF_MONTH))
                            allItems2.add(new WorldSituationItem(nationNm, natDefCnt, natDeathCnt, areaNm, stdDay));
                    }
                }

            } catch (ParserConfigurationException | SAXException | IOException | ParseException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("Main", "size=" + adapter.items.size());
                    recyclerView.setAdapter(adapter);

                    layout_content.setVisibility(View.VISIBLE);
                    layout_progress.setVisibility(View.GONE);

                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy년 MM월 dd일 hh시");
                    Calendar cal = Calendar.getInstance();

                    try {
                        Date date = sdf2.parse(allItems.get(0).getStdDay());
                        cal.setTime(date);
                        tv_date.setText(cal.get(Calendar.YEAR) + "년 " + (cal.get(Calendar.MONTH)+1) + "월 " + cal.get(Calendar.DAY_OF_MONTH) + "일");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private String getTagValue(String tag, Element element) {
        try {
            NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node node = nodes.item(0);
            return node.getNodeValue();
        } catch (Exception e){
            return null;
        }
    }
}