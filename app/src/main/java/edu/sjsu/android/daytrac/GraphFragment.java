package edu.sjsu.android.daytrac;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class GraphFragment extends Fragment {

    private static FragmentListener mFragmentListener;
    private ArrayList<TaskHistory> inputTaskHistories;
    private BarChart chart;
    private TextView text;
    private Button closeButton;

    public GraphFragment() {
        inputTaskHistories = MainActivity.getTaskHistories();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_graph, container, false);


        if(inputTaskHistories.size() == 0){
            text = rootview.findViewById(R.id.noDataText);
            text.setVisibility(View.VISIBLE);
        }else{
            chart = (rootview.findViewById(R.id.chart));
            addChartData();
        }

        closeButton = rootview.findViewById(R.id.closeButton2);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentListener.onCloseFragment(2);
            }
        });

        return rootview;
    }

    //Convert taskHistories to go in entries
    private void thToData(){

        List<BarEntry> successGroup = new ArrayList<>();
        List<BarEntry> failGroup = new ArrayList<>();

        for(int i = 0; i < inputTaskHistories.size(); i++) {
            // turn your data into Entry objects
            successGroup.add(new BarEntry(i, inputTaskHistories.get(i).getSuccesses()));
            failGroup.add(new BarEntry(i, inputTaskHistories.get(i).getFailures()));
        }

        BarDataSet set1 = new BarDataSet(successGroup, "Success");
        set1.setColor(getResources().getColor(R.color.green));
        BarDataSet set2 = new BarDataSet(failGroup, "Fail");
        set2.setColor(getResources().getColor(R.color.red));
        buildChart(set1, set2);
    }

    public void addChartData(){
        thToData();
    }

    public void buildChart(BarDataSet set1, BarDataSet set2){
        float groupSpace = 0.1f;
        float barSpace = 0.00f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
        BarData data = new BarData(set1, set2);
        data.setBarWidth(barWidth); // set the width of each bar
        chart.setData(data);
        chart.groupBars(0.00f, groupSpace, barSpace); // perform the "explicit" grouping

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum((float)inputTaskHistories.size());
        xAxis.setSpaceMax(0.1f);
        xAxis.setSpaceMin(0.1f);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        chart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(MainActivity.taskHistoriesStrings));


        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGranularity(1f);

        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setText("");
        // refresh
        chart.invalidate();
    }

    public static GraphFragment newInstance(FragmentListener fragmentListener) {
        mFragmentListener = fragmentListener;
        return new GraphFragment();
    }

    public interface FragmentListener{
        void onCloseFragment(int choice);
    }


}
