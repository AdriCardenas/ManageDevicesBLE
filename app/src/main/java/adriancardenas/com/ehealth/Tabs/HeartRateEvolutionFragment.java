package adriancardenas.com.ehealth.Tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import adriancardenas.com.ehealth.Database.DatabaseOperations;
import adriancardenas.com.ehealth.R;
import adriancardenas.com.ehealth.Utils.StringDateWithHoursComparator;

public class HeartRateEvolutionFragment extends Fragment {
    LineChart lineChart;

    public HeartRateEvolutionFragment() {

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.heart_rate_tab, container, false);
        return view;
    }

    private void drawLine() {
        lineChart = getActivity().findViewById(R.id.heart_rate_chart);
        if (lineChart != null) {
            if (!getDataSet().isEmpty()) {
                lineChart.setVisibility(View.VISIBLE);

                Description description = new Description();
                description.setText(getString(R.string.heart_rate_evolution));
                lineChart.setDescription(description);

                LineDataSet lineDataSet = new LineDataSet(getDataSet(), getString(R.string.heart_rate_evolution));
                lineDataSet.setDrawFilled(true);
                lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                LineData lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);

                //Hide grid layout

                lineChart.getAxisLeft().setDrawGridLines(false);
                lineChart.getAxisRight().setDrawGridLines(false);
                lineChart.getXAxis().setDrawGridLines(false);
                lineChart.getXAxis().setDrawLabels(false);
                lineChart.disableScroll();
                lineChart.setScaleEnabled(false);
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

        HashMap<String, Integer> rates = databaseOperations.getHeartRates();
        List<String> dates = new ArrayList<>(rates.keySet());
        Collections.sort(dates, new StringDateWithHoursComparator());
        for (String d : dates) {
            list.add(new Entry(i, rates.get(d)));
            i++;
        }
        return list;
    }
}
