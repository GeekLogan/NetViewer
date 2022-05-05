/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */
package com.mycompany.imagej;

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

    public Process_Pixels() {
        System.out.println("Setting up LiveViewer...");
        image = IJ.createHyperStack("Test", 2304, 2304, 3, 1, 1, 16);
        image.show();
    }

    @Override
    public void run(String arg) {
        System.out.println("Running LiveViewer");

        while (true) {
            URL server_url;
            try {
                System.out.println("Starting frame download...");
                server_url = new URL("http://10.156.2.28:8081");
                URLConnection conn = server_url.openConnection();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                System.out.print("Reading buffer from download... ");
                InputStream is = conn.getInputStream();
                int nRead;
                byte[] data = new byte[4096];
                while ((nRead = is.readNBytes(data, 0, data.length)) != 0) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                byte[] targetArray = buffer.toByteArray();
                System.out.println("Total data read: " + targetArray.length);

                for (int channel = 1; channel <= 3; channel++) {
                    ImageProcessor processor = image.getImageStack().getProcessor(channel);
                    short[] pixels = (short[]) processor.getPixels();
                    final int idx = 2304 * 2304 * 2 * channel;
                    for (int i = 0; i < 2304 * 2304; i++) {
                        int val = (int) targetArray[idx + (2 * i)] << 8;
                        val += (int) targetArray[idx + (2 * i) + 1]; /// TODO: check
                        pixels[i] = (short) val;
                    }
                    pixels[0] = 0;
                }
                
                image.resetDisplayRange();
                image.updateAndDraw();
            } catch (Exception ex) {
                // ¯\_(ツ)_/¯
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
