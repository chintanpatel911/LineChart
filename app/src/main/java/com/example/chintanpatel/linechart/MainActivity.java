package com.example.chintanpatel.linechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.PointsGraphSeries;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;


/*public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);

    }*/


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static int RANGE = 4;
    private SensorManager sensorManager;
    private PointsGraphSeries<DataPoint> mSeries1;
    private long graph2LastXValue = 0;
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSesnorData();
        setContentView(R.layout.activity_main);

        graph = (GraphView) findViewById(R.id.graph);
        mSeries1 = new PointsGraphSeries<DataPoint>();
        graph.addSeries(mSeries1);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);

        mSeries1.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(5);
                canvas.drawLine(x - 20, y - 20, x + 20, y + 20, paint);
                canvas.drawLine(x + 20, y - 20, x - 20, y + 20, paint);
            }
        });

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(RANGE);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setMinY(0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                long ts = event.timestamp;
                if (0 == graph2LastXValue)
                    graph2LastXValue = ts;
                float nts = (ts - graph2LastXValue) * 0.000000001f;
                System.out.println("ts = " + nts);

                if (nts > RANGE) {
                    graph2LastXValue = ts;
                    DataPoint[] dps = new DataPoint[1];
                    dps[0] = new DataPoint(0, 0);
                    mSeries1.resetData(dps);
                    nts = 0;
                }

                DataPoint dataPoint = new DataPoint(nts, y);
                mSeries1.appendData(dataPoint, false, 100);
                break;
        }
    }

    private void getSesnorData() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this,
                sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}









