package it.jaschke.alexandria.CameraPreview;

/*
 * Barebones implementation of displaying camera preview.
 *
 * Created by lisah0 on 2012-02-24
 */

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A basic Camera preview class
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.PreviewCallback previewCallback;
    private Camera.AutoFocusCallback autoFocusCallback;
    private List<Size> mSupportedPreviewSizes = new ArrayList<>();

    public CameraPreview(Context context, Camera camera,
                         Camera.PreviewCallback previewCb,
                         Camera.AutoFocusCallback autoFocusCb) {
        super(context);
        mCamera = camera;
        previewCallback = previewCb;
        autoFocusCallback = autoFocusCb;

        /*
         * Set camera to continuous focus if supported, otherwise use
         * software auto-focus. Only works for API level >=9.
         */
        Camera.Parameters parameters = camera.getParameters();
        for (String f : parameters.getSupportedFocusModes()) {
            if (f == Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
                parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                mCamera.setParameters(parameters);
                autoFocusCallback = null;
                break;
            }
        }

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            Log.d("DBG", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Camera preview released in activity
        if(mCamera!=null){
            // Call stopPreview() to stop updating the preview surface
            mCamera.stopPreview();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        /*
         * If your preview can change or rotate, take care of those events here.
         * Make sure to stop the preview before resizing or reformatting it.
         */
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        try {
            // Hard code camera surface rotation 90 degs to match Activity view in portrait
            mCamera.setDisplayOrientation(90);

            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(previewCallback);
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCallback);
        } catch (Exception e) {
            Log.d("DBG", "Error starting camera preview: " + e.getMessage());
        }
    }

    public void setCamera(Camera camera) {
        if (mCamera == camera) return;

        stopPreviewAndFreeCamera();

        mCamera = camera;

        if (mCamera != null) {
            List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportedPreviewSizes = localSizes;
            requestLayout();

            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            /*
            *   Important: call startPreview() to start updating the preview surface.
            *   Preview must be started before you can take a picture.
             */
            mCamera.startPreview();
        }

    }

    private void stopPreviewAndFreeCamera() {
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface
            mCamera.stopPreview();

            /*
            * Important: call release() to release the camera for use by other applications.
            * Applications should release the camera immediately during onPause() and re-open()
            * it during onResume()
             */
            mCamera.release();

            mCamera = null;
        }
    }
}
