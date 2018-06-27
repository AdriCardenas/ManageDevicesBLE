package adriancardenas.com.ehealth.Tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import adriancardenas.com.ehealth.Adapters.GenericRecyclerAdapter;
import adriancardenas.com.ehealth.Database.DatabaseOperations;
import adriancardenas.com.ehealth.R;
import adriancardenas.com.ehealth.Utils.StringDateComparator;
import adriancardenas.com.ehealth.Utils.StringDateWithHoursComparator;
import adriancardenas.com.ehealth.Utils.Utils;
import adriancardenas.com.ehealth.model.TableRow;

/**
 * Created by Adrian on 21/06/2018.
 */

public class DistanceEvolutionFragment extends Fragment {
    RecyclerView table;
    LineChart lineChart;

    public DistanceEvolutionFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawLine();
        drawTable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.distance_tab, container, false);
        return view;
    }

    private void drawTable() {
        table = getActivity().findViewById(R.id.table_layout_distance);
        DatabaseOperations databaseOperations = DatabaseOperations.getInstance(getContext());
        HashMap<String, Float> distances = databaseOperations.getDistances();

        List<TableRow> tableRowList = new ArrayList<>();

        List<String> dates = new ArrayList<>(distances.keySet());
        Collections.sort(dates, new StringDateComparator());

        for (String date : dates) {
            TableRow tableRow =
                    new TableRow(date, String.format("%.1f", distances.get(date)));
            tableRowList.add(tableRow);
        }

        GenericRecyclerAdapter genericRecyclerAdapter = new GenericRecyclerAdapter(tableRowList);
        table.setLayoutManager(new LinearLayoutManager(getContext()));
        table.setAdapter(genericRecyclerAdapter);
    }

    private void drawLine() {
        lineChart = getActivity().findViewById(R.id.distance_chart);
        if (lineChart != null) {
            if (!getDataSet().isEmpty()) {
                lineChart.setVisibility(View.VISIBLE);

                Description description = new Description();
                description.setText(getString(R.string.distance_evolution));
                lineChart.setDescription(description);

                LineDataSet lineDataSet = new LineDataSet(getDataSet(), getString(R.string.distance_evolution));
                lineDataSet.setDrawFilled(true);
                lineDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                LineData lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);

                //Hide grid layout
                lineChart.getAxisLeft().setDrawGridLines(false);
                lineChart.getAxisRight().setDrawGridLines(false);
                lineChart.getXAxis().setDrawGridLines(false);
                lineChart.getXAxis().setDrawLabels(true);
                lineChart.disableScroll();
                lineChart.setScaleEnabled(false);

                lineChart.getXAxis().setDrawLabels(false);
                lineChart.getAxisRight().setDrawLabels(false);
                lineChart.getAxisLeft().setTextColor(ColorTemplate.getHoloBlue());
//                xAxis.setValueFormatter(new DateValueFormatter());
//            lineChart.getLegend().setEnabled(false);

                lineChart.animateXY(1000, 1000);
                lineChart.invalidate();
            } else {
                lineChart.setVisibility(View.GONE);
            }
        }

    }

    private List<Entry> getDataSet() {
        List<Entry> list = new ArrayList<>();
        DatabaseOperations databaseOperations = DatabaseOperations.getInstance(getContext());
        int i = 1;

        HashMap<String, Float> distances = databaseOperations.getDistances();
        List<String> dates = new ArrayList<>(distances.keySet());
        Collections.sort(dates, new StringDateComparator());
        for (String d : dates) {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            try {
                Date date = format.parse(d);
                list.add(new Entry(i, distances.get(d)));
                i++;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public class DateValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM");
            return dateFormat.format(new Date(Float.valueOf(value).longValue()));
        }
    }
}
