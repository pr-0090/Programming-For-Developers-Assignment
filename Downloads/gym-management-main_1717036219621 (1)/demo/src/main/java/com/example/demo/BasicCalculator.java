package com.example.demo;

/*
Question2 a)

You are tasked with implementing a basic calculator with a graphical user interface (GUI) in Java. The calculator
should be able to evaluate valid mathematical expressions entered by the user and display the result on the GUI.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BasicCalculator implements ActionListener {

    JFrame frame;
    JTextField textfield;
    JButton[] numButtons = new JButton[10];
    JButton[] functionButtons = new JButton[8];
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, equButton, delButton, clrButton;
    JPanel panel;

    Font myFont = new Font("Times New Roman", Font.BOLD, 30);

    double num1 = 0, num2 = 0, result = 0;
    String operator = "";

    BasicCalculator() {

        frame = new JFrame(" Basic Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(null);

        textfield = new JTextField();
        textfield.setBounds(20, 25, 350, 50);
        textfield.setFont(myFont);
        textfield.setEditable(true);

        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decButton = new JButton(".");
        equButton = new JButton("=");
        delButton = new JButton("Clear");
        clrButton = new JButton("ClearAll");

        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = equButton;
        functionButtons[6] = delButton;
        functionButtons[7] = clrButton;

        for (int i = 0; i < 8; i++) {
            functionButtons[i].addActionListener(this);
            functionButtons[i].setFont(myFont);
            functionButtons[i].setFocusable(false);
        }

        for (int i = 0; i < 10; i++) {
            numButtons[i] = new JButton(String.valueOf(i));
            numButtons[i].addActionListener(this);
            numButtons[i].setFont(myFont);
            numButtons[i].setFocusable(false);
        }

        // Creating the panel to hold buttons
        panel = new JPanel();
        panel.setBounds(20, 100, 350, 400);
        panel.setLayout(new GridLayout(4, 4, 10, 10)); // 4 rows and 4 columns of buttons

        // Adding buttons to the panel
        panel.add(numButtons[1]);
        panel.add(numButtons[2]);
        panel.add(numButtons[3]);
        panel.add(addButton);
        panel.add(numButtons[4]);
        panel.add(numButtons[5]);
        panel.add(numButtons[6]);
        panel.add(subButton);
        panel.add(numButtons[7]);
        panel.add(numButtons[8]);
        panel.add(numButtons[9]);
        panel.add(mulButton);
        panel.add(decButton);
        panel.add(numButtons[0]);
        panel.add(equButton);
        panel.add(divButton);

        // Add components to the frame
        frame.add(textfield);
        frame.add(panel);
        frame.add(delButton);
        frame.add(clrButton);

        delButton.setBounds(20, 510, 150, 50);
        clrButton.setBounds(200, 510, 150, 50);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new BasicCalculator();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button clicks
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numButtons[i]) {
                textfield.setText(textfield.getText().concat(String.valueOf(i)));
            }
        }

        if (e.getSource() == decButton) {
            textfield.setText(textfield.getText().concat("."));
        }

        if (e.getSource() == addButton) {
            num1 = Double.parseDouble(textfield.getText());
            operator = "+";
            textfield.setText("");
        }

        if (e.getSource() == subButton) {
            num1 = Double.parseDouble(textfield.getText());
            operator = "-";
            textfield.setText("");
        }

        if (e.getSource() == mulButton) {
            num1 = Double.parseDouble(textfield.getText());
            operator = "*";
            textfield.setText("");
        }

        if (e.getSource() == divButton) {
            num1 = Double.parseDouble(textfield.getText());
            operator = "/";
            textfield.setText("");
        }

        if (e.getSource() == equButton) {
            num2 = Double.parseDouble(textfield.getText());

            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    result = num1 / num2;
                    break;
            }

            textfield.setText(String.valueOf(result));
            num1 = result;
            operator = "";
        }

        if (e.getSource() == clrButton) {
            textfield.setText("");
        }

        if (e.getSource() == delButton) {
            String text = textfield.getText();
            if (text.length() > 0) {
                textfield.setText(text.substring(0, text.length() - 1));
            }
        }
    }
}