package vn.edu.hcmus.fit.pmtoan;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static vn.edu.hcmus.fit.pmtoan.Utils.*;

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

    private JButton slang_search_btn;
    private JButton definition_search_btn;
    private JTextField search_input;
    private DefaultListModel list_slang;
    private JList slang_result;
    private DefaultListModel list_history;
    private JList search_history;
    private JTextArea definition;

    private JTextField slang_word_input;
    private JTextField definition_input;
    private JButton add_btn;
    private JButton update_btn;
    private JButton delete_btn;
    private JButton reset_btn;
    private JButton delete_history_btn;

    Map<String, String> dictionary = new HashMap<>();

    String historyFile = "history.txt";
    String slangOriginFile = "slang.txt";
    String slangEditFile = "edit.txt";

    public UI(){
        dictionary = readOriginFile(slangOriginFile);
        readEditFile(slangEditFile, dictionary);
        List<String> history = readHistoryFile(historyFile);
        list_history = new DefaultListModel();
        for(String str : history){
            list_history.addElement(str);
        }

        prepareGUI();
    }

    public void prepareGUI(){
        mainFrame = new JFrame("Dictionary");
        //mainFrame.setSize(1300,700);
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

        mainFrame.pack();
        mainFrame.setVisible(true);
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

        //-------------------------- SEARCH FIELD -----------------------------\\
        search_input = new JTextField(30);
        search_input.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        search_input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                StringBuilder text = new StringBuilder(search_input.getText().trim());
                text.append(e.getKeyChar());
                if(e.getKeyChar() == (char)8){
                    System.out.println(text.length());
                    text.deleteCharAt(text.length() - 1);
                }

                HashSet<String> keySet = new HashSet<>(dictionary.keySet());
                List<String> listResult = searchBySlang(text.toString(), keySet);
                list_slang.clear();
                list_slang.addAll(listResult);
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        //-------------------------- SEARCH BUTTON -----------------------------\\
        JLabel label_search= new JLabel("Search by");
        label_search.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));

        slang_search_btn = new JButton("slang");
        slang_search_btn.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        slang_search_btn.setFocusable(false);
        slang_search_btn.addActionListener(this);

        definition_search_btn = new JButton("definition");
        definition_search_btn.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        definition_search_btn.setFocusable(false);
        definition_search_btn.addActionListener(this);

        JPanel search_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25,10));
        search_panel.add(label_search);
        search_panel.add(slang_search_btn);
        search_panel.add(definition_search_btn);

        //-------------------------- SEARCH RESULT -----------------------------\\
        JLabel label_slang= new JLabel("Slang list");
        label_slang.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        label_slang.setForeground(new Color(15, 175, 15));

        list_slang = new DefaultListModel();
        slang_result = new JList(list_slang);
        slang_result.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        slang_result.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!slang_result.isSelectionEmpty() && !e.getValueIsAdjusting()){
                    int idx = slang_result.getSelectedIndex();

                    String key = list_slang.getElementAt(idx).toString();
                    String def = dictionary.get(key);

                    definition.setText(def);

                    list_history.addElement(key);

                    saveHistorySearch(historyFile, key, true);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(slang_result);
        scrollPane.setPreferredSize(new Dimension(450,250));

        JPanel slang_panel = new JPanel(new BorderLayout(10,10));
        slang_panel.add(label_slang, BorderLayout.NORTH);
        slang_panel.add(scrollPane, BorderLayout.CENTER);

        JLabel label_definition = new JLabel("Definition");
        label_definition.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        label_definition.setForeground(new Color(211, 15, 15));

        definition = new JTextArea();
        definition.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        definition.setLineWrap(true);
        definition.setWrapStyleWord(true);

        JScrollPane scrollPane2 = new JScrollPane(definition);
        scrollPane2.setPreferredSize(new Dimension(450,200));

        JPanel definition_panel = new JPanel(new BorderLayout(10,10));
        definition_panel.add(label_definition, BorderLayout.NORTH);
        definition_panel.add(scrollPane2, BorderLayout.CENTER);

        //-------------------------- HISTORY SEARCH -----------------------------\\
        JLabel history_label = new JLabel("History Search");
        history_label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
        history_label.setForeground(Color.PINK);

        search_history = new JList(list_history);
        search_history.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));

        JScrollPane scrollPane3 = new JScrollPane(search_history);
        scrollPane3.setPreferredSize(new Dimension(150,580));

        delete_history_btn = new JButton("delete history");
        delete_history_btn.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        delete_history_btn.setFocusable(false);
        delete_history_btn.addActionListener(this);
        delete_history_btn.setForeground(new Color(211, 15, 15));
        delete_history_btn.setBounds(new Rectangle(10,10));

        JPanel history_panel = new JPanel(new BorderLayout(10,10));
        history_panel.add(history_label, BorderLayout.NORTH);
        history_panel.add(scrollPane3, BorderLayout.CENTER);
        history_panel.add(delete_history_btn, BorderLayout.SOUTH);

        //-------------------------- LAYOUT SETUP -----------------------------\\
        GridBagConstraints gbc = new GridBagConstraints();
        //gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10,15,10,15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(panel_label, gbc);
        gbc.gridy = 1;
        panel.add(search_input, gbc);
        gbc.gridy = 2;
        panel.add(search_panel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(slang_panel, gbc);
        gbc.gridy = 4;
        panel.add(definition_panel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 4;
        panel.add(history_panel, gbc);

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
        slang_word_input = new JTextField(col);
        slang_word_input.setFont(new Font(font, Font.PLAIN, size_text));
        //-------------- slang_word --------------------------------------

        //-------------- definition --------------------------------------
        JLabel definition_label = new JLabel("Definition");
        definition_label.setFont(new Font(font, Font.PLAIN, size_text));
        definition_input = new JTextField(col);
        definition_input.setFont(new Font(font, Font.PLAIN, size_text));
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
        input.add(slang_word_input, gbc);
        gbc.gridy = 1;
        input.add(definition_input, gbc);


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
        else if(source.equals(slang_search_btn)){
            HashSet<String> keySet = new HashSet<>(dictionary.keySet());
            List<String> listResult = searchBySlang(search_input.getText(), keySet);
            definition.setText("");
            list_slang.clear();
            list_slang.addAll(listResult);
        }
        else if(source.equals(definition_search_btn)){
            List<String> listResult = searchByDefinition(search_input.getText(), dictionary);
            definition.setText("");
            list_slang.clear();
            list_slang.addAll(listResult);
        }
        else if(source.equals(delete_history_btn)){
            list_history.clear();
            saveHistorySearch(historyFile, "", false);
        }
    }
}
