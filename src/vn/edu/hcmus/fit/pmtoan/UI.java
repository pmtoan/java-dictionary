package vn.edu.hcmus.fit.pmtoan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * vn.edu.hcmus.fit.pmtoan
 * Create by pmtoan
 * Date 12/21/2021 - 10:19 AM
 * Description: ...
 */
public class UI implements ActionListener {
    private JFrame mainFrame;

    JPanel searchPanel;
    JPanel editPanel;

    private JButton search_func;
    private JButton edit_func;
    private JButton minigame_func;

    private JButton search_btn;
    private JTextField search_filed;
    private JList search_result;
    private JList search_history;

    private JTextField slang_word;
    private JTextField definition;
    private JButton add_btn;
    private JButton update_btn;
    private JButton delete_btn;
    private JButton reset_btn;

    public UI(){
        prepareGUI();
    }

    public void prepareGUI(){
        mainFrame = new JFrame("Dictionary");
        mainFrame.setSize(1100,600);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(mainFrame,
                        "Do you want to Exit ?", "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                } else if (result == JOptionPane.NO_OPTION){
                    mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        mainFrame.setVisible(true);
    }

    public void showUI(){
        JPanel functionPanel = functionPanel();
        JPanel workPanel = new JPanel();

        searchPanel = searchPanel();
        editPanel = editPanel();

        workPanel.add(searchPanel);
        workPanel.add(editPanel);

        searchPanel.setVisible(true);
        editPanel.setVisible(false);

        mainFrame.add(functionPanel, BorderLayout.WEST);
        mainFrame.add(workPanel, BorderLayout.CENTER);
    }
    private JPanel functionPanel(){
        JPanel panel = new JPanel(new GridBagLayout());

        search_func = new JButton("SEARCH");
        search_func.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        search_func.setPreferredSize(new Dimension(200,50));
        search_func.setFocusable(false);
        search_func.addActionListener(this);
        edit_func = new JButton("EDIT");
        edit_func.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        edit_func.setPreferredSize(new Dimension(200,50));
        edit_func.setFocusable(false);
        edit_func.addActionListener(this);
        minigame_func = new JButton("MINIGAME");
        minigame_func.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        minigame_func.setPreferredSize(new Dimension(200,50));
        minigame_func.setFocusable(false);
        minigame_func.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30,30,30,30);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(search_func, gbc);
        gbc.gridy = 1;
        panel.add(edit_func, gbc);
        gbc.gridy = 2;
        panel.add(minigame_func, gbc);

        return panel;
    }

    private JPanel searchPanel(){
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel panel_label = new JLabel("SEARCH");
        panel_label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));

        search_btn = new JButton("search");
        search_btn.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        search_btn.setFocusable(false);
        search_btn.addActionListener(this);

        search_filed = new JTextField(30);
        search_filed.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));

        search_result = new JList();

        search_history = new JList();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(15,15,15,15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(panel_label, gbc);
        gbc.gridy = 1;
        panel.add(search_filed, gbc);
        gbc.gridx = 1;
        panel.add(search_btn, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(search_result, gbc);
        gbc.gridx = 1;
        panel.add(search_history, gbc);

        return panel;
    }

    private JPanel editPanel(){
        int col = 25;
        int size_text = 20;
        String font = Font.MONOSPACED;

        JLabel panel_label = new JLabel("EDIT");
        panel_label.setFont(new Font(font, Font.PLAIN, 40));
        
        //-------------- slang_word --------------------------------------
        JLabel slang_label = new JLabel("Slang word");
        slang_label.setFont(new Font(font, Font.PLAIN, size_text));
        slang_word = new JTextField(col);
        slang_word.setFont(new Font(font, Font.PLAIN, size_text));
        //-------------- slang_word --------------------------------------

        //-------------- definition --------------------------------------
        JLabel definition_label = new JLabel("Definition");
        definition_label.setFont(new Font(font, Font.PLAIN, size_text));
        definition = new JTextField(col);
        definition.setFont(new Font(font, Font.PLAIN, size_text));
        //-------------- definition font
        
        JPanel input = new JPanel();
        input.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5,5,5,10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 10;
        input.add(slang_label, gbc);
        gbc.gridy = 1;
        input.add(definition_label, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        input.add(slang_word, gbc);
        gbc.gridy = 1;
        input.add(definition, gbc);
   

        add_btn = new JButton("ADD");
        add_btn.setFont(new Font("", Font.BOLD, size_text));
        add_btn.setBackground(Color.green);
        add_btn.setPreferredSize(new Dimension(150,35));
        add_btn.setFocusable(false);
        add_btn.addActionListener(this);

        update_btn = new JButton("UPDATE");
        update_btn.setFont(new Font("", Font.BOLD, size_text));
        update_btn.setBackground(Color.yellow);
        update_btn.setPreferredSize(new Dimension(150,35));
        update_btn.setFocusable(false);
        update_btn.addActionListener(this);

        delete_btn = new JButton("DELETE");
        delete_btn.setFont(new Font("", Font.BOLD, size_text));
        delete_btn.setBackground(Color.red);
        delete_btn.setPreferredSize(new Dimension(150,35));
        delete_btn.setFocusable(false);
        delete_btn.addActionListener(this);

        reset_btn = new JButton("RESET");
        reset_btn.setFont(new Font("", Font.BOLD, size_text));
        reset_btn.setBackground(Color.ORANGE);
        reset_btn.setPreferredSize(new Dimension(150,35));
        reset_btn.setFocusable(false);
        reset_btn.addActionListener(this);

        JPanel btn = new JPanel();
        btn.setLayout(new FlowLayout());
        btn.add(add_btn);
        btn.add(update_btn);
        btn.add(delete_btn);
        btn.add(reset_btn);

        JPanel result = new JPanel();
        result.setLayout(new GridBagLayout());
        gbc.insets = new Insets(5,5,35,0);
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        result.add(new JPanel().add(panel_label), gbc);
        gbc.gridy = 1;
        result.add(input, gbc);
        gbc.gridy = 2;
        result.add(btn, gbc);

        return result;
    }

    public static void main(String[] args){
        UI mainProgram = new UI();
        mainProgram.showUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (search_func.equals(source)) {
            searchPanel.setVisible(true);
            editPanel.setVisible(false);
        }
        else if (edit_func.equals(source)) {
            searchPanel.setVisible(false);
            editPanel.setVisible(true);
        }
        else if (minigame_func.equals(source)) {
        }
    }
}
