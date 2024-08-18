package com.example.demo;

/*
Question 6

This scenario presents another sample Java GUI application using multithreading and an asynchronous framework
(SwingWorker) to demonstrate asynchronous progress updates and batch processing.
*/


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileBatchProcessor extends JFrame {

    private JTable statusTable;
    private DefaultTableModel tableModel;
    private JProgressBar overallProgressBar;
    private JButton startButton, cancelButton;
    private JFileChooser fileChooser;
    private JComboBox<String> formatComboBox;
    private List<File> filesToProcess;
    private ExecutorService executorService;
    private List<Future<?>> futures;
    private AtomicBoolean isCancelled;

    public FileBatchProcessor() {
        setTitle("File Batch Processor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Initialize components
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        formatComboBox = new JComboBox<>(new String[]{"PDF to Docx", "Image Resize"});
        startButton = new JButton("Start");
        cancelButton = new JButton("Cancel");
        overallProgressBar = new JProgressBar(0, 100);
        overallProgressBar.setValue(0);
        overallProgressBar.setStringPainted(true);

        // Status table
        tableModel = new DefaultTableModel(new String[]{"File", "Conversion Type", "Progress"}, 0);
        statusTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(statusTable);

        // Layout setup
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JPanel filePanel = new JPanel();
        filePanel.add(new JLabel("Select files:"));
        JButton selectFilesButton = new JButton("Browse");
        filePanel.add(selectFilesButton);
        filePanel.add(new JLabel("Format:"));
        filePanel.add(formatComboBox);

        panel.add(filePanel);
        panel.add(tableScrollPane);
        panel.add(overallProgressBar);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel);

        setContentPane(panel);

        // Add listeners
        selectFilesButton.addActionListener(e -> chooseFiles());
        startButton.addActionListener(e -> startProcessing());
        cancelButton.addActionListener(e -> cancelProcessing());

        // Initialize executor service and futures
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        futures = new ArrayList<>();
        isCancelled = new AtomicBoolean(false);
    }

    private void chooseFiles() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            filesToProcess = List.of(fileChooser.getSelectedFiles());
            tableModel.setRowCount(0);
            for (File file : filesToProcess) {
                tableModel.addRow(new Object[]{file.getName(), formatComboBox.getSelectedItem(), "0%"});
            }
        }
    }

    private void startProcessing() {
        if (filesToProcess == null || filesToProcess.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No files selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        isCancelled.set(false);
        overallProgressBar.setValue(0);
        futures.clear();

        for (int i = 0; i < filesToProcess.size(); i++) {
            File file = filesToProcess.get(i);
            String format = (String) formatComboBox.getSelectedItem();

            int rowIndex = i;
            SwingWorker<Void, Integer> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    // Simulate processing work
                    int steps = 100;
                    for (int step = 0; step < steps; step++) {
                        if (isCancelled.get()) return null;
                        Thread.sleep(50); // Simulate time-consuming task
                        publish((step + 1) * 100 / steps);
                    }
                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    int progress = chunks.get(chunks.size() - 1);
                    SwingUtilities.invokeLater(() -> {
                        tableModel.setValueAt(progress + "%", rowIndex, 2);
                        updateOverallProgress();
                    });
                }

                @Override
                protected void done() {
                    try {
                        get(); // Ensure any exception is caught
                    } catch (InterruptedException | ExecutionException e) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(FileBatchProcessor.this, "Error during processing", "Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                }
            };

            Future<?> future = executorService.submit(worker);
            futures.add(future);
        }
    }

    private void cancelProcessing() {
        isCancelled.set(true);
        for (Future<?> future : futures) {
            future.cancel(true);
        }
        JOptionPane.showMessageDialog(this, "Processing cancelled!", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateOverallProgress() {
        int completed = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String progress = (String) tableModel.getValueAt(i, 2);
            completed += Integer.parseInt(progress.replace("%", ""));
        }
        int average = completed / tableModel.getRowCount();
        overallProgressBar.setValue(average);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileBatchProcessor frame = new FileBatchProcessor();
            frame.setVisible(true);
        });
    }
}
