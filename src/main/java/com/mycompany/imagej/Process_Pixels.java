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
        System.out.println("In run.");
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

        /*
        ImagePlus image = IJ.createHyperStack("Test", 2304, 2304, 3, 1, 1, 16);
        image.show();

        for (int i = 1; i <= 3; i++) {
            ImageProcessor processor = image.getImageStack().getProcessor(i);
            short[] pixels = (short[]) processor.getPixels();
            for (int j = 0; j <= 2304 * 30; j++) {
                pixels[j] = 1;
            }
        }*/

        /*
        System.out.println("Hello World;");
        
        URL yahoo = new URL("http://google.com");
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            System.out.println(inputLine);
        in.close();
         */
    }
}
