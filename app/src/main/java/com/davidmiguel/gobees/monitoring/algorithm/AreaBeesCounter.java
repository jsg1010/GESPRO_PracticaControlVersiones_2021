
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
package com.davidmiguel.gobees.monitoring.algorithm;	package com.davidmiguel.gobees.monitoring.algorithm;


import android.support.annotation.NonNull;	import android.support.annotation.NonNull;
import android.util.Log;


import com.davidmiguel.gobees.logging.Log;
import com.davidmiguel.gobees.monitoring.algorithm.processors.BackgroundSubtractor;	import com.davidmiguel.gobees.monitoring.algorithm.processors.BackgroundSubtractor;
import com.davidmiguel.gobees.monitoring.algorithm.processors.Blur;	import com.davidmiguel.gobees.monitoring.algorithm.processors.Blur;
import com.davidmiguel.gobees.monitoring.algorithm.processors.ContoursFinder;	import com.davidmiguel.gobees.monitoring.algorithm.processors.ContoursFinder;
import com.davidmiguel.gobees.monitoring.algorithm.processors.Morphology;	import com.davidmiguel.gobees.monitoring.algorithm.processors.Morphology;
import org.opencv.core.Mat;	import org.opencv.core.Mat;
/**	/**
 * Counts the number of bees based on the area of detected moving contours.	 * Counts the number of bees based on the area of detected moving contours.
 */	 */
public class AreaBeesCounter implements BeesCounter {	public class AreaBeesCounter implements BeesCounter {


    private static final String TAG = "ContourBeesCounter";
    private static AreaBeesCounter instance;	    private static AreaBeesCounter instance;


    private Blur blur;	    private Blur blur;
    private BackgroundSubtractor bs;	    private BackgroundSubtractor bs;
    private Morphology morphology;	    private Morphology morphology;
    private ContoursFinder cf;	    private ContoursFinder cf;
    private Mat processedFrame;	    private Mat processedFrame;
    /**	    /**
     * Default ContourBeesCounter constructor.	     * Default ContourBeesCounter constructor.
     * History is initialized to 10 and shadows threshold to 0.7.	     * History is initialized to 10 and shadows threshold to 0.7.
     * minArea is initialized to 15 and maxArea to 800.	     * minArea is initialized to 15 and maxArea to 800.
     */	     */
    private AreaBeesCounter() {	    private AreaBeesCounter() {
        blur = new Blur();	        blur = new Blur();
        bs = new BackgroundSubtractor();	        bs = new BackgroundSubtractor();
        morphology = new Morphology();	        morphology = new Morphology();
        cf = new ContoursFinder();	        cf = new ContoursFinder();
    }	    }
    public static AreaBeesCounter getInstance() {	    public static AreaBeesCounter getInstance() {
        if (instance == null) {	        if (instance == null) {
            instance = new AreaBeesCounter();	            instance = new AreaBeesCounter();
        }	        }
        return instance;	        return instance;
    }	    }
    @Override	    @Override
    public int countBees(@NonNull Mat frame) {	    public int countBees(@NonNull Mat frame) {
        final long t0 = System.nanoTime();	        final long t0 = System.nanoTime();
        Mat r0 = blur.process(frame);	        Mat r0 = blur.process(frame);
        Mat r1 = bs.process(r0);	        Mat r1 = bs.process(r0);
        Mat r2 = morphology.process(r1);	        Mat r2 = morphology.process(r1);
        processedFrame = cf.process(r2);	        processedFrame = cf.process(r2);
        r0.release();	        r0.release();
        r1.release();	        r1.release();
        r2.release();	        r2.release();
        Log.d(TAG, "countBees time: " + (System.nanoTime() - t0) / 1000000);	        Log.d("countBees time: %f", (System.nanoTime() - t0) / 1000000);
        return cf.getNumBees();	        return cf.getNumBees();
    }	    }


    @Override	    @Override
    public Mat getProcessedFrame() {	    public Mat getProcessedFrame() {
        return processedFrame;	        return processedFrame;
    }	    }
    @Override	    @Override
    public void updateBlobSize(BlobSize size) {	    public void updateBlobSize(BlobSize size) {
        switch (size) {	        switch (size) {
            case SMALL:	            case SMALL:
                morphology.setDilateKernel(2);	                morphology.setDilateKernel(2);
                morphology.setErodeKernel(3);	                morphology.setErodeKernel(3);
                break;	                break;
            case NORMAL:	            case NORMAL:
                morphology.setDilateKernel(3);	                morphology.setDilateKernel(3);
                morphology.setErodeKernel(3);	                morphology.setErodeKernel(3);
                break;	                break;
            case BIG:	            case BIG:
            default:	            default:
                morphology.setDilateKernel(3);	                morphology.setDilateKernel(3);
                morphology.setErodeKernel(2);	                morphology.setErodeKernel(2);
        }	        }
    }	    }
    @Override	    @Override
    public void updateMinArea(Double minArea) {	    public void updateMinArea(Double minArea) {
        cf.setMinArea(minArea);	        cf.setMinArea(minArea);
    }	    }
    @Override	    @Override
    public void updateMaxArea(Double maxArea) {	    public void updateMaxArea(Double maxArea) {
        cf.setMaxArea(maxArea);	        cf.setMaxArea(maxArea);
    }	    }
}	}
