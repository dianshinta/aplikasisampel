package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.Controller;

public class UpdateFixPoinPanel extends JPanel {
    private JLabel pointsLabel;
    private JLabel updateYourPoints;
    private JSpinner pointsSpinner;
    private JButton updateButton;
    private Controller controller;

    public UpdateFixPoinPanel(Controller controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        setBackground(new Color(234,231,224)); // Light orange background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Adding padding between components
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        pointsLabel = new JLabel("Total Poin Pelanggaran: "); // Initialize without 0
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set font and style

        updateYourPoints = new JLabel("Perbarui Poin Anda: ");
        updateYourPoints.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font and style

        pointsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1)); // Set spinner model
        pointsSpinner.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font and style

        updateButton = new JButton("Simpan");
        updateButton.setBackground(new Color(42, 51, 66)); // Light yellow background
        updateButton.setFont(new Font("Arial", Font.BOLD, 14)); // Set font and style
        updateButton.setForeground(Color.WHITE); // Set text color

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(pointsLabel, gbc);

        gbc.gridy = 1;
        add(updateYourPoints, gbc);

        gbc.gridx = 1;
        add(pointsSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span both columns
        gbc.anchor = GridBagConstraints.CENTER; // Center align the button
        add(updateButton, gbc);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newPoints = (Integer) pointsSpinner.getValue();
                controller.updateUserPoints(newPoints);
            }
        });
    }

    public void updatePointsLabel(int updatedPoints) {
        pointsLabel.setText("Total Poin Pelanggaran: " + updatedPoints);
    }
}
