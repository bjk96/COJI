package com.cojigae.coji.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cojigae.coji.R;
import com.cojigae.coji.adapter.PubReliefHospServiceAdapter;
import com.cojigae.coji.item.PubReliefHospServiceItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ScreeningCenterFragment extends Fragment {
    private Context context;
    Handler handler = new Handler();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    List<PubReliefHospServiceItem> pubServiceItem = new ArrayList<>();
    private ImageButton button;
    private EditText inputText;
    List<PubReliefHospServiceItem> equalsList = new ArrayList<>();

    private LinearLayout layout_content;
    private ConstraintLayout layout_progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();

        View view = inflater.inflate(R.layout.fragment_screening_center, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        inputText= view.findViewById(R.id.searchText);

        layout_content = view.findViewById(R.id.layout_hosp_content);
        layout_progress = view.findViewById(R.id.layout_hosp_progress);

        button = view.findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMsg = inputText.getText().toString();
                search(inputMsg);
            }
        });

        inputText.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                search(v.getText().toString());
                return true;
            }
            return false;
        });

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));

        HospThread hThread = new HospThread();
        hThread.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_baseline_info_24);
        builder.setTitle("정보");
        builder.setMessage("지도 아이콘 클릭 시 일부 앱에서는 검색 결과가 나오지 않을 수 있습니다.");
        builder.setPositiveButton("확인", (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();

        return view;
    }

    @Override
    public void onPause() {
        inputText.setText("");
        super.onPause();
    }

    private class HospThread extends Thread{
        public void run(){
            pubServiceItem.clear();


            String url = "http://apis.data.go.kr/B551182/pubReliefHospService/getpubReliefHospList?serviceKey="
                    + serviceKey
                    + "&pageNo=1&numOfRows=1000&spclAdmTyCd=99";

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
                        String yadmNm = getTagValue("yadmNm", element);
                        String sidoNm = getTagValue("sidoNm", element);
                        String sgguNm = getTagValue("sgguNm", element);
                        String telno = getTagValue("telno", element);

                        pubServiceItem.add(new PubReliefHospServiceItem(yadmNm, sidoNm, sgguNm, telno));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    hospService();

                    layout_content.setVisibility(View.VISIBLE);
                    layout_progress.setVisibility(View.GONE);
                }
            });
        }
    }

    private void hospService() {
        PubReliefHospServiceAdapter adapter = new PubReliefHospServiceAdapter(pubServiceItem);
        recyclerView.setAdapter(adapter);
    }

    private void search(String input) {
        if (input.isEmpty()) {
            equalsList.clear();
            hospService();
            Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            equalsList.clear();

            for (PubReliefHospServiceItem pubReliefHospServiceItem : pubServiceItem) {
                if (pubReliefHospServiceItem.getYadmNm().contains(input) || pubReliefHospServiceItem.getSidoNm().contains(input) || pubReliefHospServiceItem.getSgguNm().contains(input)) {
                    equalsList.add(pubReliefHospServiceItem);
                    equal();
                }
            }
        }

        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
    }

    public void equal() {
        PubReliefHospServiceAdapter adapter = new PubReliefHospServiceAdapter(equalsList);
        recyclerView.setAdapter(adapter);
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