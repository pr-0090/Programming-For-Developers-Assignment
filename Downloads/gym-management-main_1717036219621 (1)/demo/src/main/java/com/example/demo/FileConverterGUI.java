package com.example.demo;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class FileConverterGUI extends JFrame implements ActionListener {
    private JButton selectFilesButton, startButton, cancelButton;
    private JProgressBar overallProgressBar;
    private JTextArea statusArea;
    private JFileChooser fileChooser;
    private JComboBox<String> conversionOptionsComboBox;
    private List<File> filesToConvert = new ArrayList<>();
    private List<FileConversionWorker> workers = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public FileConverterGUI() {
        setTitle("File Converter");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for file selection and options
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        selectFilesButton = new JButton("Select Files");
        selectFilesButton.addActionListener(this);
        topPanel.add(selectFilesButton);

        conversionOptionsComboBox = new JComboBox<>(new String[] {"PDF to DOCX", "Image Resize"});
        topPanel.add(conversionOptionsComboBox);

        startButton = new JButton("Start Conversion");
        startButton.addActionListener(this);
        topPanel.add(startButton);

        cancelButton = new JButton("Cancel All");
        cancelButton.addActionListener(this);
        topPanel.add(cancelButton);

        add(topPanel, BorderLayout.NORTH);

        // Progress and status
        overallProgressBar = new JProgressBar();
        overallProgressBar.setStringPainted(true);
        add(overallProgressBar, BorderLayout.SOUTH);

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectFilesButton) {
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                filesToConvert = Arrays.asList(fileChooser.getSelectedFiles());
                updateStatus("Files selected: " + filesToConvert.size());
            }
        } else if (e.getSource() == startButton) {
            startConversion();
        } else if (e.getSource() == cancelButton) {
            cancelAllConversions();
        }
    }

    private void startConversion() {
        updateStatus("Starting conversions...");
        overallProgressBar.setValue(0);
        overallProgressBar.setMaximum(filesToConvert.size());
        workers.clear();

        for (File file : filesToConvert) {
            FileConversionWorker worker = new FileConversionWorker(file, (String) conversionOptionsComboBox.getSelectedItem());
            workers.add(worker);
            worker.execute();
        }
    }

    private void cancelAllConversions() {
        for (FileConversionWorker worker : workers) {
            worker.cancel(true);
        }
        updateStatus("All conversions cancelled.");
    }

    private void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> statusArea.append(message + "\n"));
    }

    private class FileConversionWorker extends SwingWorker<Void, String> {
        private File file;
        private String conversionType;
        private boolean cancelled = false;

        public FileConversionWorker(File file, String conversionType) {
            this.file = file;
            this.conversionType = conversionType;
        }

        @Override
        protected Void doInBackground() throws Exception {
            // Simulate file conversion process
            int totalSteps = 100; // Simulated steps
            for (int i = 1; i <= totalSteps; i++) {
                if (isCancelled()) {
                    cancelled = true;
                    break;
                }
                Thread.sleep(50); // Simulate time-consuming task
                publish("Processing " + file.getName() + ": " + i + "%");
                setProgress(i);
            }
            if (!cancelled) {
                publish("Completed " + file.getName());
            }
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            for (String message : chunks) {
                updateStatus(message);
            }
        }

        @Override
        protected void done() {
            if (cancelled) {
                updateStatus("Cancelled " + file.getName());
            } else {
                updateStatus("Finished " + file.getName());
                overallProgressBar.setValue(overallProgressBar.getValue() + 1);
                if (overallProgressBar.getValue() == overallProgressBar.getMaximum()) {
                    JOptionPane.showMessageDialog(FileConverterGUI.this, "All conversions are complete!");
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileConverterGUI gui = new FileConverterGUI();
            gui.setVisible(true);
        });
    }
}
