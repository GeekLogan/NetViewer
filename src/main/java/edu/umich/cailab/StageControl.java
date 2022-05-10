/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package edu.umich.cailab;

import com.google.gson.Gson;
import ij.plugin.PlugIn;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author loganaw
 */
public class StageControl extends javax.swing.JFrame implements PlugIn {

    public static void print_error(String err) {
        System.err.println(err);
    }

    public static byte[] read_all_buffer(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead = 0;
        byte[] data = new byte[4096];
        while ((nRead = is.read(data)) != -1) {
            buffer.write(data, 0, nRead);
        }
        is.close();
        buffer.flush();
        return buffer.toByteArray();
    }

    public static String get_http(String url) {
        URL server_url;
        try {
            server_url = new URL(url);
        } catch (MalformedURLException ex) {
            print_error("Malformed URL Exception found.");
            return "ERROR";
        }

        URLConnection conn;
        byte[] targetArray;
        try {
            System.out.println("Starting frame download...");
            conn = server_url.openConnection();
            InputStream is = conn.getInputStream();
            targetArray = read_all_buffer(is);
        } catch (IOException ex) {
            print_error("IO Exception found.");
            return "ERROR";
        }

        System.out.println("Total data read: " + targetArray.length);
        System.out.println("\tEx: " + targetArray[0] + ", " + targetArray[1] + ", " + targetArray[2]);

        return new String(targetArray, StandardCharsets.UTF_8);
    }

    public static Map<?, ?> parse_json(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Map.class);
    }

    /**
     * Creates new form StageControl
     */
    public StageControl() {
        initComponents();
        current_positions = new HashMap<>();
        current_positions.put(1, 0.0);
        current_positions.put(2, 0.0);
        current_positions.put(3, 0.0);

        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider();
        jSlider2 = new javax.swing.JSlider();
        jSlider3 = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(194, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StageControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StageControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StageControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StageControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        StageControl window = new StageControl();
        window.run("");
    }

    @Override
    public void run(String string) {
        System.out.println("Yeet!");

        while (true) {
            this.update_positions();
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
            }
        }
    }

    public void update_positions() {
        String positions = get_http("http://10.156.2.107:5000/get_positions");
        Map<?, ?> positions_map = parse_json(positions);
        for (Map.Entry<?, ?> entry : positions_map.entrySet()) {
            int k = Integer.parseInt((String) entry.getKey());
            double v = (double) entry.getValue();
            current_positions.replace(k, v);
        }
        System.out.println("New positions = " + current_positions);
    }

    private Map<Integer, Double> current_positions;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    // End of variables declaration//GEN-END:variables
}
