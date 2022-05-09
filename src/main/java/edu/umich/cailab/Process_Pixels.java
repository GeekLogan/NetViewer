/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */
package edu.umich.cailab;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A template for processing each pixel of either GRAY8, GRAY16, GRAY32 or
 * COLOR_RGB images.
 *
 * @author Johannes Schindelin
 */
public class Process_Pixels implements PlugIn {

    protected ImagePlus image;
    protected LiveViewControl control_window;

    public Process_Pixels() {
        System.out.println("Setting up LiveViewer...");
        control_window = new LiveViewControl();
        image = IJ.createHyperStack("Test", 2304, 2304, 3, 1, 1, 16);
        image.show();
    }

    @Override
    public void run(String arg) {
        System.out.println("Running LiveViewer");

        while (true) {
            if (control_window.getLiveViewToggle()) {
                URL server_url;
                try {
                    System.out.println("Starting frame download...");
                    server_url = new URL("http://10.156.2.28:8081");
                    URLConnection conn = server_url.openConnection();
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                    System.out.print("Reading buffer from download... ");
                    InputStream is = conn.getInputStream();
                    int nRead = 0;
                    byte[] data = new byte[4096];
                    while ((nRead = is.read(data)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    is.close();

                    buffer.flush();
                    byte[] targetArray = buffer.toByteArray();
                    System.out.println("Total data read: " + targetArray.length);
                    System.out.println("\tEx: " + targetArray[0] + ", " + targetArray[1] + ", " + targetArray[2]);

                    for (int channel = 1; channel <= 3; channel++) {
                        ImageProcessor processor = image.getImageStack().getProcessor(channel);
                        short[] pixels = (short[]) processor.getPixels();

                        final int idx = 2304 * 2304 * 2 * (channel - 1); // why imagej????
                        for (int i = 0; i < 2304 * 2304; i++) {
                            int val = targetArray[idx + (2 * i)];
                            //val += (int) targetArray[idx + (2 * i) + 1] << 8; /// TODO: check
                            pixels[i] = (short) val;
                        }
                    } 

                    image.resetDisplayRange();
                    image.updateAndDraw();
                } catch (Exception ex) {
                    System.err.println(ex);
                    System.err.println("Exception caught.");
                    // ¯\_(ツ)_/¯
                }
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // Who cares?
                }
            }
        }
    }

    /**
     * Main method for debugging.
     *
     * For debugging, it is convenient to have a method that starts ImageJ,
     * loads an image and calls the plugin, e.g. after setting breakpoints.
     *
     * @param args unused
     */
    public static void main(String[] args) throws Exception {
        // set the plugins.dir property to make the plugin appear in the Plugins menu
        // see: https://stackoverflow.com/a/7060464/1207769
        Class<?> clazz = Process_Pixels.class;
        java.net.URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
        java.io.File file = new java.io.File(url.toURI());
        System.setProperty("plugins.dir", file.getAbsolutePath());

        // start ImageJ
        ImageJ IJ_instance = new ImageJ();

        // open the Clown sample
        //ImagePlus image = IJ.openImage("http://imagej.net/images/clown.jpg");
        //image.show();
        // run the plugin
        IJ.runPlugIn(clazz.getName(), "");
    }
}
