/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.socialbrothers.android.imageRecognitionSB;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductManager;
import com.socialbrothers.android.imageRecognitionSB.View.ShoppingCartActivity;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import static com.socialbrothers.android.imageRecognitionSB.Alternatives.EDIT_PRODUCT;

/**
 * Classifies images with Tensorflow Lite.
 */
public class ImageClassifier {

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "TfLiteCameraDemo";

    /**
     * Name of the model file stored in Assets.
     */
    private static final String MODEL_PATH = "model.tflite";
    /**
     * Name of the label file stored in Assets.
     */
    public static final String LABEL_PATH = "retrained_labels.txt";

    /**
     * Number of results to show in the UI.
     */
    private static final int RESULTS_TO_SHOW = 1;
    private static final float MINIMUM_RECOGNITION_TRESHHOLD = 0.60f;
    private static final float MINIMUM_PAYMENT_TRESHHOLD = 0.80f;
    private static final String WARNING_MINIMUM_RECOGNITION_TRESHOLD = "Scan een product";
    /**
     * Dimensions of inputs.
     */
    public static final int DIM_BATCH_SIZE = 1;
    public static final int DIM_PIXEL_SIZE = 3;

    static final int DIM_IMG_SIZE_X = 224;
    static final int DIM_IMG_SIZE_Y = 224;

    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    /* Preallocated buffers for storing image data in. */
    private int[] intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];

    /**
     * An instance of the driver class to run model inference with Tensorflow Lite.
     */
    private Interpreter tflite;
    private Context context;
    private View v;
    private boolean isVisible;
    private boolean canMove;
    private TextView productName, title;
    private at.markushi.ui.CircleButton scanButton;

    /**
     * Labels corresponding to the output of the vision model.
     */
    public List<String> labelList;

    /**
     * A ByteBuffer to hold image data, to be feed into Tensorflow Lite as inputs.
     */
    private ByteBuffer imgData = null;

    /**
     * An array to hold inference results, to be feed into Tensorflow Lite as outputs.
     */
    private float[][] labelProbArray = null;
    /**
     * multi-stage low pass filter
     **/
    private float[][] filterLabelProbArray = null;
    private static final int FILTER_STAGES = 3;
    private static final float FILTER_FACTOR = 0.4f;

    public Toast lastToast = null;

    private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    (o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));

    /**
     * Initializes an {@code ImageClassifier}.
     */
    ImageClassifier(Activity activity, Context context, View v) throws IOException {
        ProductManager.Initialize(context.getResources());
        tflite = new Interpreter(loadModelFile(activity));
        this.context = context;
        this.v = v;
        labelList = loadLabelList(activity);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.averia_sans_libre_light);
        productName = v.findViewById(R.id.text);
        title = v.findViewById(R.id.title);
        productName.setTypeface(typeface);
        title.setTypeface(typeface);
        imgData =
                ByteBuffer.allocateDirect(
                        4 * DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());
        labelProbArray = new float[1][labelList.size()];
        filterLabelProbArray = new float[FILTER_STAGES][labelList.size()];

        Log.d(TAG, "Created a Tensorflow Lite Image Classifier.");
    }

    /**
     * Classifies a frame from the preview stream.
     */

    @SuppressLint("ClickableViewAccessibility")
    String classifyFrame(Bitmap bitmap) {
        if (tflite == null) {
            Log.e(TAG, "Image classifier has not been initialized; Skipped.");
            return "Uninitialized Classifier.";
        }
        productName = v.findViewById(R.id.text);
        scanButton = v.findViewById(R.id.scanButton);
        String productText = printTopKLabels();
        scanButton.setOnTouchListener((v, event) -> {
//            if(event.getAction()==MotionEvent.ACTION_DOWN){
//                if(isVisible){
//                    try{
//                        productName.setVisibility(View.INVISIBLE);
//                        Intent intent = new Intent(context, Alternatives.class);
//                        intent.putExtra(EDIT_PRODUCT, productText);
//                        context.startActivity(intent);
//                    }catch(Exception e){
//                    }
//                }
//            }else{
//            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(scanButton, "scaleX", 2f);
                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(scanButton, "scaleY", 2f);
                    RotateAnimation rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(500);
                    scanButton.startAnimation(rotate);
                    scaleDownX.setDuration(500);
                    scaleDownY.setDuration(500);

                    AnimatorSet scaleDown = new AnimatorSet();
                    scaleDown.play(scaleDownX).with(scaleDownY);

                    scaleDown.start();
                    if (isVisible) {
                        try {
                            Intent intent = new Intent(context, Alternatives.class);
                            intent.putExtra(EDIT_PRODUCT, productText);
                            context.startActivity(intent);
                        } catch (Exception e) {
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    if (isVisible) {
                        if(canMove) {
                            try {
                                Intent intent = new Intent(context, Alternatives.class);
                                intent.putExtra(EDIT_PRODUCT, productText);
                                context.startActivity(intent);
                                canMove = false;
                            } catch (Exception e) {
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(scanButton, "scaleX", 1f);
                    ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(scanButton, "scaleY", 1f);
                    RotateAnimation rotate2 = new RotateAnimation(360, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate2.setDuration(500);
                    scanButton.startAnimation(rotate2);
                    scaleDownX2.setDuration(1000);
                    scaleDownY2.setDuration(1000);

                    AnimatorSet scaleDown2 = new AnimatorSet();
                    scaleDown2.play(scaleDownX2).with(scaleDownY2);

                    scaleDown2.start();
                    canMove = true;
                    //scanButton.setEnabled(false);
                    break;

            }
            return true;
        });
//        if(isVisible){
//
//            try{
//                productName.setVisibility(View.INVISIBLE);
//                Intent intent = new Intent(context, Alternatives.class);
//                intent.putExtra(EDIT_PRODUCT, productText);
//                context.startActivity(intent);
//            }catch(Exception e){
//
//            }
//        }
        convertBitmapToByteBuffer(bitmap);
        // Here's where the magic happens!!!
        long startTime = SystemClock.uptimeMillis();
        tflite.run(imgData, labelProbArray);
        long endTime = SystemClock.uptimeMillis();
        Log.d(TAG, "Timecost to run model inference: " + Long.toString(endTime - startTime));

        // smooth the results
        applyFilter();
        // print the results
        String textToShow = printTopKLabels();
        return textToShow;
    }

    void applyFilter() {
        int num_labels = labelList.size();

        // Low pass filter `labelProbArray` into the first stage of the filter.
        for (int j = 0; j < num_labels; ++j) {
            filterLabelProbArray[0][j] += FILTER_FACTOR * (labelProbArray[0][j] -
                    filterLabelProbArray[0][j]);
        }
        // Low pass filter each stage into the next.
        for (int i = 1; i < FILTER_STAGES; ++i) {
            for (int j = 0; j < num_labels; ++j) {
                filterLabelProbArray[i][j] += FILTER_FACTOR * (
                        filterLabelProbArray[i - 1][j] -
                                filterLabelProbArray[i][j]);

            }
        }

        // Copy the last stage filter output back to `labelProbArray`.
        for (int j = 0; j < num_labels; ++j) {
            labelProbArray[0][j] = filterLabelProbArray[FILTER_STAGES - 1][j];
        }
    }

    /**
     * Closes tflite to release resources.
     */
    public void close() {
        tflite.close();
        tflite = null;
    }

    /**
     * Reads label list from Assets.
     */
    private List<String> loadLabelList(Activity activity) throws IOException {
        List<String> labelList = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(activity.getAssets().open(LABEL_PATH)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    /**
     * Memory-map the model file in Assets.
     */
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {

        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);

    }

    /**
     * Writes Image data into a {@code ByteBuffer}.
     */
    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Convert the image to floating point.
        int pixel = 0;
        long startTime = SystemClock.uptimeMillis();
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
                final int val = intValues[pixel++];
                imgData.putFloat((((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                imgData.putFloat((((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                imgData.putFloat((((val) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
            }
        }
        long endTime = SystemClock.uptimeMillis();
        Log.d(TAG, "Timecost to put values into ByteBuffer: " + Long.toString(endTime - startTime));
    }

    /**
     * Prints top-K labels, to be shown in UI as the results.
     */

    private String printTopKLabels() {
        for (int i = 0; i < labelList.size(); ++i) {
            sortedLabels.add(
                    new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        String textToShow = "";
        final int size = sortedLabels.size();
        Map.Entry<String, Float> label = sortedLabels.poll();
        for (int i = 0; i < size; ++i) {
            textToShow = String.format("\n%s", label.getKey(), label.getValue()) + textToShow;
        }
        if (label.getValue() > MINIMUM_RECOGNITION_TRESHHOLD) {
            if (label.getValue() > MINIMUM_PAYMENT_TRESHHOLD) {
                isVisible = true;
                return textToShow;
            }
            return textToShow;
        } else if (label.getValue() <= MINIMUM_PAYMENT_TRESHHOLD) isVisible = false;
        return WARNING_MINIMUM_RECOGNITION_TRESHOLD;
    }
}