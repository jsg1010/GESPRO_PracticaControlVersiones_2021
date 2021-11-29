
/*	/*
 * GoBees	 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano	 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *	 *
 * This program is free software: you can redistribute it and/or modify	 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by	 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or	 * the Free Software Foundation, either version 3 of the License, or
 * any later version.	 * any later version.
 *	 *
 * This program is distributed in the hope that it will be useful,	 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.	 * GNU General Public License for more details.
 *	 *
 * You should have received a copy of the GNU General Public License	 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.	 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */	 */
package com.davidmiguel.gobees.hive;	package com.davidmiguel.gobees.hive;
import android.content.Context;	import android.content.Context;
import android.graphics.Color;	import android.graphics.Color;
import android.graphics.drawable.Drawable;	import android.graphics.drawable.Drawable;
import android.os.Build;	import android.os.Build;
import android.support.annotation.NonNull;	import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;	import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;	import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;	import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;	import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;	import android.view.ContextMenu;
import android.view.LayoutInflater;	import android.view.LayoutInflater;
import android.view.MenuInflater;	import android.view.MenuInflater;
import android.view.MenuItem;	import android.view.MenuItem;
import android.view.View;	import android.view.View;
import android.view.ViewGroup;	import android.view.ViewGroup;
import android.widget.ImageView;	import android.widget.ImageView;
import android.widget.TextView;	import android.widget.TextView;
import com.davidmiguel.gobees.R;	import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Record;	import com.davidmiguel.gobees.data.model.Record;
import com.davidmiguel.gobees.data.model.Recording;	import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.utils.BaseViewHolder;	import com.davidmiguel.gobees.utils.BaseViewHolder;
import com.davidmiguel.gobees.utils.HourAxisValueFormatter;	import com.davidmiguel.gobees.utils.HourAxisValueFormatter;
import com.davidmiguel.gobees.utils.ItemTouchHelperViewHolder;	import com.davidmiguel.gobees.utils.ItemTouchHelperViewHolder;
import com.davidmiguel.gobees.utils.StringUtils;	import com.davidmiguel.gobees.utils.StringUtils;
import com.github.mikephil.charting.charts.LineChart;	import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;	import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;	import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;	import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;	import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;	import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;	import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.text.SimpleDateFormat;	import java.text.SimpleDateFormat;
import java.util.ArrayList;	import java.util.ArrayList;
import java.util.List;	import java.util.List;
import java.util.Locale;	import java.util.Locale;
import static com.google.common.base.Preconditions.checkNotNull;	import static com.google.common.base.Preconditions.checkNotNull;
/**	/**
 * Recordings list adapter.	 * Recordings list adapter.
 */	 */
class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.RecordingViewHolder> {	class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.RecordingViewHolder> {
    private Context context;	    private Context context;
    private MenuInflater menuInflater;	    private MenuInflater menuInflater;
    private List<Recording> recordings;	    private List<Recording> recordings;
    private RecordingItemListener listener;	    private RecordingItemListener listener;
    RecordingsAdapter(Context context, MenuInflater menuInflater,	    RecordingsAdapter(Context context, MenuInflater menuInflater,
                      List<Recording> recordings, RecordingItemListener listener) {	                      List<Recording> recordings, RecordingItemListener listener) {
        this.context = context;	        this.context = context;
        this.menuInflater = menuInflater;	        this.menuInflater = menuInflater;
        this.recordings = checkNotNull(recordings);	        this.recordings = checkNotNull(recordings);
        this.listener = listener;	        this.listener = listener;
    }	    }
    @Override	    @Override
    public RecordingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {	    public RecordingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())	        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hive_recordings_list_item, parent, false);	                .inflate(R.layout.hive_recordings_list_item, parent, false);
        return new RecordingViewHolder(view);	        return new RecordingViewHolder(view);
    }	    }
    @Override	    @Override
    public void onBindViewHolder(RecordingViewHolder holder, int position) {	    public void onBindViewHolder(RecordingViewHolder holder, int position) {
        holder.bind(recordings.get(position));	        holder.bind(recordings.get(position));
    }	    }
    @Override	    @Override
    public int getItemCount() {	    public int getItemCount() {
        return recordings == null ? 0 : recordings.size();	        return recordings == null ? 0 : recordings.size();
    }	    }
    void replaceData(List<Recording> recordings) {	    void replaceData(List<Recording> recordings) {
        this.recordings = checkNotNull(recordings);	        this.recordings = checkNotNull(recordings);
        notifyDataSetChanged();	        notifyDataSetChanged();
    }	    }
    interface RecordingItemListener {	    interface RecordingItemListener {
        void onRecordingClick(Recording clickedRecording);	        void onRecordingClick(Recording clickedRecording);
        void onRecordingDelete(Recording clickedRecording);	        void onRecordingDelete(Recording clickedRecording);
        void onOpenMenuClick(View view);	        void onOpenMenuClick(View view);
    }	    }
    class RecordingViewHolder extends RecyclerView.ViewHolder	    class RecordingViewHolder extends RecyclerView.ViewHolder
            implements BaseViewHolder<Recording>, View.OnClickListener,	            implements BaseViewHolder<Recording>, View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener,	            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener,
            ItemTouchHelperViewHolder {	            ItemTouchHelperViewHolder {
        private View viewHolder;	        private View viewHolder;
        private CardView card;	        private CardView card;
        private TextView recordingDate;	        private TextView recordingDate;
        private LineChart chart;	        private LineChart chart;
        private ImageView moreIcon;	        private ImageView moreIcon;
        private Drawable background;	        private Drawable background;
        private SimpleDateFormat formatter;	        private SimpleDateFormat formatter;
        RecordingViewHolder(View itemView) {	        RecordingViewHolder(View itemView) {
            super(itemView);	            super(itemView);
            // Get views	            // Get views
            viewHolder = itemView;	            viewHolder = itemView;
            card = (CardView) itemView.findViewById(R.id.card);	            card = (CardView) itemView.findViewById(R.id.card);
            recordingDate = (TextView) itemView.findViewById(R.id.recording_date);	            recordingDate = (TextView) itemView.findViewById(R.id.recording_date);
            chart = (LineChart) itemView.findViewById(R.id.chart);	            chart = (LineChart) itemView.findViewById(R.id.chart);
            moreIcon = (ImageView) itemView.findViewById(R.id.more_icon);	            moreIcon = (ImageView) itemView.findViewById(R.id.more_icon);
            // Set listeners	            // Set listeners
            viewHolder.setOnClickListener(this);	            viewHolder.setOnClickListener(this);
            viewHolder.setOnCreateContextMenuListener(this);	            viewHolder.setOnCreateContextMenuListener(this);
            moreIcon.setOnClickListener(new View.OnClickListener() {	            moreIcon.setOnClickListener(new View.OnClickListener() {
                @Override	                @Override
                public void onClick(View view) {	                public void onClick(View view) {
                    // Open Menu	                    // Open Menu
                    listener.onOpenMenuClick(viewHolder);	                    listener.onOpenMenuClick(viewHolder);
                }	                }
            });	            });
            background = card.getBackground();	            background = card.getBackground();
            formatter = new SimpleDateFormat(	            formatter = new SimpleDateFormat(
                    context.getString(R.string.hive_recordings_date_format), Locale.getDefault());	                    context.getString(R.string.hive_recordings_date_format), Locale.getDefault());
        }	        }
        @Override	        @Override
        public void bind(@NonNull final Recording recording) {	        public void bind(@NonNull final Recording recording) {
            // Title	            // Title
            String date = formatter.format(recording.getDate());	            String date = formatter.format(recording.getDate());
            recordingDate.setText(StringUtils.capitalize(date));	            recordingDate.setText(StringUtils.capitalize(date));
            // Chart	            // Chart
            if (recording.getRecords() != null && !recording.getRecords().isEmpty()) {	            if (recording.getRecords() != null && !recording.getRecords().isEmpty()) {
                long firstTimestamp = recording.getRecords().get(0).getTimestamp().getTime() / 1000;	                long firstTimestamp = recording.getRecords().get(0).getTimestamp().getTime() / 1000;
                List<Entry> entries = getChartData(recording.getRecords(), firstTimestamp);	                List<Entry> entries = getChartData(recording.getRecords(), firstTimestamp);
                LineData data = styleChartLines(entries);	                LineData data = styleChartLines(entries);
                setupChart(chart, data, firstTimestamp);	                setupChart(chart, data, firstTimestamp);
            }	            }
        }	        }
        @Override	        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view,	        public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                        ContextMenu.ContextMenuInfo contextMenuInfo) {	                                        ContextMenu.ContextMenuInfo contextMenuInfo) {
            // Inflate menu	            // Inflate menu
            menuInflater.inflate(R.menu.recording_item_menu, contextMenu);	            menuInflater.inflate(R.menu.recording_item_menu, contextMenu);
            // Set click listener	            // Set click listener
            for (int i = 0; i < contextMenu.size(); i++) {	            for (int i = 0; i < contextMenu.size(); i++) {
                contextMenu.getItem(i).setOnMenuItemClickListener(this);	                contextMenu.getItem(i).setOnMenuItemClickListener(this);
            }	            }
        }	        }
        @Override	        @Override
        public void onClick(View view) {	        public void onClick(View view) {
            listener.onRecordingClick(recordings.get(getAdapterPosition()));	            listener.onRecordingClick(recordings.get(getAdapterPosition()));
        }	        }
        @Override	        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {	        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.menu_delete) {	            if (menuItem.getItemId() == R.id.menu_delete) {
                listener.onRecordingDelete(recordings.get(getAdapterPosition()));	                listener.onRecordingDelete(recordings.get(getAdapterPosition()));
                return true;	                return true;
            }	            }
            return false;	            return false;
        }	        }
        @Override	        @Override
        public void onItemSelected() {	        public void onItemSelected() {
            card.setBackgroundColor(Color.LTGRAY);	            card.setBackgroundColor(Color.LTGRAY);
        }	        }
        @Override	        @Override
        public void onItemClear() {	        public void onItemClear() {
            card.setBackground(background);	            card.setBackground(background);
        }	        }
        /**	        /**
         * Get and prepare the data to draw in the chart.	         * Get and prepare the data to draw in the chart.
         *	         *
         * @param recordsList    list of records.	         * @param recordsList    list of records.
         * @param firstTimestamp seconds timestamp of the first record (used as initial reference).	         * @param firstTimestamp seconds timestamp of the first record (used as initial reference).
         * @return list of entries.	         * @return list of entries.
         */	         */
        private List<Entry> getChartData(List<Record> recordsList, long firstTimestamp) {	        private List<Entry> getChartData(List<Record> recordsList, long firstTimestamp) {
            Record[] records = recordsList.toArray(new Record[recordsList.size()]);	            Record[] records = recordsList.toArray(new Record[recordsList.size()]);
            List<Entry> entries = new ArrayList<>();	            List<Entry> entries = new ArrayList<>();
            for (Record record : records) {	            for (Record record : records) {
                // Convert timestamp to seconds and relative to first timestamp	                // Convert timestamp to seconds and relative to first timestamp
                long timestamp = record.getTimestamp().getTime() / 1000 - firstTimestamp;	                long timestamp = record.getTimestamp().getTime() / 1000 - firstTimestamp;
                entries.add(new Entry(timestamp, record.getNumBees()));	                entries.add(new Entry(timestamp, record.getNumBees()));
            }	            }
            return entries;	            return entries;
        }	        }
        /**	        /**
         * Style char lines (type, color, etc.).	         * Style char lines (type, color, etc.).
         *	         *
         * @param entries list of entries.	         * @param entries list of entries.
         * @return line data chart.	         * @return line data chart.
         */	         */
        private LineData styleChartLines(List<Entry> entries) {	        private LineData styleChartLines(List<Entry> entries) {
            // Set styles	            // Set styles
            LineDataSet lineDataSet = new LineDataSet(entries, "Recording");	            LineDataSet lineDataSet = new LineDataSet(entries, "Recording");
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);	            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineDataSet.setCubicIntensity(0.2f);	            lineDataSet.setCubicIntensity(0.2f);
            lineDataSet.setDrawValues(false);	            lineDataSet.setDrawValues(false);
            lineDataSet.setDrawCircles(false);	            lineDataSet.setDrawCircles(false);
            lineDataSet.setLineWidth(1.8f);	            lineDataSet.setLineWidth(1.8f);
            lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorAccent));	            lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorAccent));
            if (((int) lineDataSet.getYMax()) != 0) {	            if (((int) lineDataSet.getYMax()) != 0) {
                lineDataSet.setDrawFilled(true);	                lineDataSet.setDrawFilled(true);
                lineDataSet.setFillAlpha(255);	                lineDataSet.setFillAlpha(255);
                // Fix bug with vectors in API < 21	                // Fix bug with vectors in API < 21
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){	                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
                    Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),	                    Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),
                            R.drawable.chart_fade, null);	                            R.drawable.chart_fade, null);
                    lineDataSet.setFillDrawable(drawable);	                    lineDataSet.setFillDrawable(drawable);
                } else{	                } else{
                    lineDataSet.setFillColor(ContextCompat.getColor(context, R.color.colorPrimary));	                    lineDataSet.setFillColor(ContextCompat.getColor(context, R.color.colorPrimary));
                }	                }
            }	            }
            return new LineData(lineDataSet);	            return new LineData(lineDataSet);
        }	        }
        /**	        /**
         * Setup chart (axis, grid, etc.).	         * Setup chart (axis, grid, etc.).
         *	         *
         * @param lineChart      chart to setup.	         * @param lineChart      chart to setup.
         * @param data           chart with the data.	         * @param data           chart with the data.
         * @param firstTimestamp seconds timestamp of the first record (used as initial reference).	         * @param firstTimestamp seconds timestamp of the first record (used as initial reference).
         */	         */
        private void setupChart(LineChart lineChart, LineData data, long firstTimestamp) {	        private void setupChart(LineChart lineChart, LineData data, long firstTimestamp) {
            // General setup	            // General setup
            lineChart.setDrawGridBackground(false);	            lineChart.setDrawGridBackground(false);
            lineChart.setDrawBorders(false);	            lineChart.setDrawBorders(false);
            lineChart.setViewPortOffsets(50, 0, 50, 50);	            lineChart.setViewPortOffsets(50, 0, 50, 50);
            lineChart.getDescription().setEnabled(false);	            lineChart.getDescription().setEnabled(false);
            lineChart.getLegend().setEnabled(false);	            lineChart.getLegend().setEnabled(false);
            lineChart.setTouchEnabled(false);	            lineChart.setTouchEnabled(false);
            lineChart.setNoDataText(context.getString(R.string.no_flight_act_data_available));	            lineChart.setNoDataText(context.getString(R.string.no_flight_act_data_available));
            // X axis setup	            // X axis setup
            IAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(firstTimestamp);	            IAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(firstTimestamp);
            XAxis xAxis = lineChart.getXAxis();	            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(xAxisFormatter);	            xAxis.setValueFormatter(xAxisFormatter);
            xAxis.setDrawGridLines(false);	            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);	            xAxis.setDrawAxisLine(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);	            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setCenterAxisLabels(false);	            xAxis.setCenterAxisLabels(false);
            xAxis.setTextColor(ContextCompat.getColor(context, R.color.colorIcons));	            xAxis.setTextColor(ContextCompat.getColor(context, R.color.colorIcons));
            // Y axis setup	            // Y axis setup
            YAxis yAxis = lineChart.getAxisLeft();	            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setAxisMaximum(40);	            yAxis.setAxisMaximum(40);
            yAxis.setAxisMinimum(0);	            yAxis.setAxisMinimum(0);
            yAxis.setDrawLabels(false);	            yAxis.setDrawLabels(false);
            yAxis.setDrawAxisLine(false);	            yAxis.setDrawAxisLine(false);
            yAxis.setDrawGridLines(true);	            yAxis.setDrawGridLines(true);
            lineChart.getAxisRight().setEnabled(false);	            lineChart.getAxisRight().setEnabled(false);
            // Add data	            // Add data
            lineChart.setData(data);	            lineChart.setData(data);
        }	        }
    }	    }
}	
