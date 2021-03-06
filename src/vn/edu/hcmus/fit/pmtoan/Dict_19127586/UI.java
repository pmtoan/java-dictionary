package vn.edu.hcmus.fit.pmtoan.Dict_19127586;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static vn.edu.hcmus.fit.pmtoan.Dict_19127586.Utils.*;

/**
 * vn.edu.hcmus.fit.pmtoan
 * Create by pmtoan
 * Date 12/21/2021 - 10:19 AM
 * Description: ...
 */
public class UI implements ActionListener {
    public static void main(String[] args){
        UI mainProgram = new UI();
        mainProgram.showUI();
    }

    Map<String, List<String>> dictionary;
    Map<String, List<String>> defMap;
    Trie slangStore;

    // HBTY : happy birthday to you | happy birthday to u
    // HNY : happy new year
    // HD: happy day
    // O(1)
    // search: happy new


    private JFrame mainFrame;

    JPanel searchPanel;
    private JButton slangSearchButton;
    private JButton definitionSearchButton;
    private JTextField searchInput;
    private JTextArea definition;
    private final DefaultListModel slangListModel;
    private JList slangResult;
    private final DefaultListModel historyListModel;
    private JList searchHistory;

    JPanel editPanel;
    private JTextField slangInput;
    private JTextArea definitionInput;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton resetButton;
    private JButton deleteHistoryButton;
    private final DefaultTableModel editTableModel;
    private final JTable slangTable;
    private String oldSlang = "";
    private String oldDefinition = "";
    private int selectedRow = -1;

    JPanel miniGamePanel;
    private JPanel slangOfTheDayPanel;
    private JPanel miniGame1Panel;
    private JPanel miniGame2Panel;
    private JButton slangOfTheDayButton;
    private JButton miniGame1Button;
    private JButton miniGame2Button;
    private int answerOfMiniGame1;
    private int answerOfMiniGame2;

    String historyFile = "history.txt";
    String slangOriginFile = "slang.txt";
    String slangCloneFile = "slang_clone.txt";

    public UI(){
        dictionary = new HashMap<>();
        defMap = new HashMap<>();
        readCloneFile(slangOriginFile, slangCloneFile, dictionary, defMap);
        slangStore = new Trie();
        slangStore.addList(dictionary.keySet());

        slangListModel = new DefaultListModel();
        slangListModel.addAll(dictionary.keySet());

        historyListModel = new DefaultListModel();
        historyListModel.addAll(readHistoryFile(historyFile));

        String[] colName = {"Slang", "Definition"};
        String[][] data = {};
        editTableModel = new DefaultTableModel(data, colName);

        slangTable = new JTable(editTableModel);
        fillDataToTable(slangOriginFile, slangCloneFile);

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
                } else{
                    mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    public void showUI(){
        JPanel workPanel = new JPanel();

        searchPanel = searchPanel();
        editPanel = editPanel();
        miniGamePanel = miniGamePanel();

        workPanel.add(searchPanel);
        workPanel.add(editPanel);
        workPanel.add(miniGamePanel);

        mainFrame.add(tabbedPane(), BorderLayout.NORTH);
        mainFrame.add(workPanel, BorderLayout.CENTER);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private JTabbedPane tabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        /* add three tab with three JPanel */
        tabbedPane.addTab("SEARCH", null, searchPanel, "click to show search tab");
        tabbedPane.addTab("EDIT", null, editPanel, "click to show edit tab");
        tabbedPane.addTab("MINI-GAME", null, miniGamePanel, "click to show mini-game tab");

        tabbedPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        tabbedPane.setFocusable(false);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                searchInput.setText("");
                slangInput.setText("");
                definitionInput.setText("");
                definition.setText("");
                if (e.getSource() instanceof JTabbedPane) {
                    JTabbedPane pane = (JTabbedPane) e.getSource();
                    if(pane.getSelectedIndex() == 0 || pane.getSelectedIndex() == 2){
                        slangListModel.addAll(dictionary.keySet());
                    };
                }
            }
        });

        return tabbedPane;
    }

    private JPanel searchPanel(){
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel panel_label = new JLabel("SEARCH");
        panel_label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));

        //-------------------------- SEARCH FIELD -----------------------------\\
        searchInput = new JTextField(40);
        searchInput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        searchInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                StringBuilder text = new StringBuilder(searchInput.getText());
                text.insert(searchInput.getCaretPosition(), e.getKeyChar());
                if(e.getKeyChar() == (char)8){
                    text.deleteCharAt(searchInput.getCaretPosition());
                    definition.setText("");
                }

                HashSet<String> keySet = new HashSet<>(dictionary.keySet());
                List<String> listResult = searchBySlang(text.toString(), slangStore);
                slangListModel.clear();
                slangListModel.addAll(listResult);
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

        slangSearchButton = new JButton("slang");
        slangSearchButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        slangSearchButton.setFocusable(false);
        slangSearchButton.addActionListener(this);

        definitionSearchButton = new JButton("definition");
        definitionSearchButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        definitionSearchButton.setFocusable(false);
        definitionSearchButton.addActionListener(this);

        JPanel search_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25,10));
        search_panel.add(label_search);
        search_panel.add(slangSearchButton);
        search_panel.add(definitionSearchButton);

        //-------------------------- SEARCH RESULT -----------------------------\\
        JLabel label_slang= new JLabel("Slang list");
        label_slang.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        label_slang.setForeground(new Color(15, 175, 15));

        slangResult = new JList(slangListModel);
        slangResult.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));
        slangResult.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!slangResult.isSelectionEmpty() && !e.getValueIsAdjusting()){
                    int idx = slangResult.getSelectedIndex();

                    String key = slangListModel.getElementAt(idx).toString();
                    String def = "";
                    List<String> values = dictionary.get(key);
                    for(int i=0; i< values.size(); i++){
                        def += values.get(i);
                        def += i == (values.size() - 1) ? "" : " | ";
                    }

                    definition.setText(def);

                    historyListModel.addElement(key);

                    saveHistorySearch(historyFile, key, true);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(slangResult);
        scrollPane.setPreferredSize(new Dimension(650,400));

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
        definition.setEditable(false);

        JScrollPane scrollPane2 = new JScrollPane(definition);
        scrollPane2.setPreferredSize(new Dimension(650,200));

        JPanel definition_panel = new JPanel(new BorderLayout(10,10));
        definition_panel.add(label_definition, BorderLayout.NORTH);
        definition_panel.add(scrollPane2, BorderLayout.CENTER);

        //-------------------------- HISTORY SEARCH -----------------------------\\
        JLabel history_label = new JLabel("History Search");
        history_label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
        history_label.setForeground(Color.PINK);

        searchHistory = new JList(historyListModel);
        searchHistory.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));

        JScrollPane scrollPane3 = new JScrollPane(searchHistory);
        scrollPane3.setPreferredSize(new Dimension(250,730));

        deleteHistoryButton = new JButton("delete history");
        deleteHistoryButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        deleteHistoryButton.setFocusable(false);
        deleteHistoryButton.addActionListener(this);
        deleteHistoryButton.setForeground(new Color(211, 15, 15));
        deleteHistoryButton.setBounds(new Rectangle(10,10));

        JPanel history_panel = new JPanel(new BorderLayout(10,10));
        history_panel.add(history_label, BorderLayout.NORTH);
        history_panel.add(scrollPane3, BorderLayout.CENTER);
        history_panel.add(deleteHistoryButton, BorderLayout.SOUTH);

        //-------------------------- LAYOUT SETUP -----------------------------\\
        GridBagConstraints gbc = new GridBagConstraints();
        //gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10,15,10,15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(panel_label, gbc);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(searchInput, gbc);
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
        int col = 40;
        int size_text = 25;
        String font = Font.MONOSPACED;

        JLabel panelLabel = new JLabel("EDIT");
        panelLabel.setFont(new Font(font, Font.PLAIN, 40));

        //---------------------- Slang input -----------------------------\\
        JLabel slangLabel = new JLabel("Slang word");
        slangLabel.setFont(new Font(font, Font.PLAIN, size_text));
        slangInput = new JTextField(col);
        slangInput.setFont(new Font(font, Font.PLAIN, size_text));
        slangInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                StringBuilder text = new StringBuilder(slangInput.getText());
                text.insert(slangInput.getCaretPosition(), e.getKeyChar());
                if(e.getKeyChar() == (char)8){
                    text.deleteCharAt(slangInput.getCaretPosition());
                }

                TableRowSorter sorter = new TableRowSorter(editTableModel);
                slangTable.setRowSorter(sorter);
                if(text.length() == 0){
                    sorter.setRowFilter(null);
                } else{
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        //------------------------- Definition input -------------------------\\
        JLabel definitionLabel = new JLabel("Definition");
        definitionLabel.setFont(new Font(font, Font.PLAIN, size_text));
        definitionInput = new JTextArea();
        definitionInput.setFont(new Font(font, Font.PLAIN, size_text));
        definitionInput.setLineWrap(true);
        definitionInput.setWrapStyleWord(true);
        JScrollPane scrollDefinition = new JScrollPane(definitionInput);
        scrollDefinition.setPreferredSize(new Dimension(0, 150));

        //------------------------- Input setup -------------------------\\
        JPanel input = new JPanel();
        input.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5,5,5,10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 10;
        input.add(slangLabel, gbc);
        gbc.gridy = 1;
        input.add(definitionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        input.add(slangInput, gbc);
        gbc.gridy = 1;
        input.add(scrollDefinition, gbc);

        //------------------------- Button -------------------------\\
        addButton = new JButton("ADD");
        addButton.setFont(new Font(font, Font.BOLD, size_text));
        addButton.setBackground(Color.green);
        addButton.setPreferredSize(new Dimension(150,35));
        addButton.setFocusable(false);
        addButton.addActionListener(this);

        updateButton = new JButton("UPDATE");
        updateButton.setFont(new Font(font, Font.BOLD, size_text));
        updateButton.setBackground(Color.yellow);
        updateButton.setPreferredSize(new Dimension(150,35));
        updateButton.setFocusable(false);
        updateButton.addActionListener(this);

        deleteButton = new JButton("DELETE");
        deleteButton.setFont(new Font(font, Font.BOLD, size_text));
        deleteButton.setBackground(Color.red);
        deleteButton.setPreferredSize(new Dimension(150,35));
        deleteButton.setFocusable(false);
        deleteButton.addActionListener(this);

        resetButton = new JButton("RESET");
        resetButton.setFont(new Font(font, Font.BOLD, size_text));
        resetButton.setBackground(Color.ORANGE);
        resetButton.setPreferredSize(new Dimension(150,35));
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        //------------------------- Button setup -------------------------\\
        JPanel btn = new JPanel();
        btn.setLayout(new FlowLayout());
        btn.add(addButton);
        btn.add(updateButton);
        btn.add(deleteButton);
        btn.add(resetButton);

        //------------------------- Table of slang -------------------------\\
        slangTable.setFont(new Font(font, Font.PLAIN, 20));
        slangTable.getTableHeader().setFont(new Font(font, Font.PLAIN, size_text));
        slangTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        slangTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        slangTable.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        slangTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                actionWhenInteractTable();
            }
        });

        JScrollPane scroll_list = new JScrollPane(slangTable);
        scroll_list.setPreferredSize(new Dimension(750,450));

        //------------------------- Edit panel setup -------------------------\\
        JPanel result = new JPanel();
        result.setLayout(new GridBagLayout());
        gbc.insets = new Insets(5,5,35,0);
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        result.add(new JPanel().add(panelLabel), gbc);
        gbc.gridy = 1;
        result.add(input, gbc);
        gbc.gridy = 2;
        result.add(btn, gbc);
        gbc.gridy = 3;
        result.add(scroll_list, gbc);

        return result;
    }

    private void fillDataToTable(String originFile, String cloneFile){
        List<Dictionary> listSlang = readCloneFileToTable(originFile, cloneFile);

        editTableModel.setRowCount(0);
        for (Dictionary slang : listSlang) {
            editTableModel.addRow(new Object[]{slang.getSlang(), slang.getDefinition()});
        }
        TableRowSorter sorter = new TableRowSorter(editTableModel);
        slangTable.setRowSorter(sorter);
        sorter.setRowFilter(null);
    }

    private void updateTable(String slang, String definition, String action){
        TableRowSorter sorter = new TableRowSorter(editTableModel);
        slangTable.setRowSorter(sorter);
        sorter.setRowFilter(null);

        if(action.equals("add")){
            editTableModel.addRow(new Object[]{slang, definition});
        }else if(action.equals("update")){
            editTableModel.setValueAt(slang, selectedRow, 0);
            editTableModel.setValueAt(definition, selectedRow, 1);
        }else if(action.equals("delete")){
            editTableModel.removeRow(selectedRow);
        }else if(action.equals("overwrite")){
            for(int i=0; i<editTableModel.getRowCount(); i++){
                if(editTableModel.getValueAt(i, 0).equals(slang)){
                    editTableModel.removeRow(i);
                    i--;
                }
            }
            editTableModel.addRow(new Object[]{slang, definition});
        }
    }

    private void updateMap(String slang, String definition, String newDefinition, String action){
        if(action.equals("add")){
            MapUtils.addNewSlangToDictionary(slang, definition, dictionary, defMap);
            slangStore.addWord(slang);
        }
        else if(action.equals("duplicate")){
            MapUtils.addDuplicateSlangToDictionary(slang, definition, dictionary, defMap);
        }
        else if(action.equals("update")){
            MapUtils.removeDefinitionOutOfMap(slang, definition, dictionary, defMap);
            MapUtils.addNewDefinition(slang, newDefinition, dictionary, defMap);
        }
        else if(action.equals("delete")){
            MapUtils.deleteOneSlang(slang, definition, dictionary, defMap);
            if(!dictionary.containsKey(slang)){
                slangStore.delete(slang);
            }
        }
        else if(action.equals("overwrite")){
           MapUtils.deleteDuplicateSlang(slang, dictionary, defMap);
            updateMap(slang, definition, "","add");
        }
    }

    private void actionWhenInteractTable() {
        selectedRow = slangTable.convertRowIndexToModel(slangTable.getSelectedRow());

        slangInput.setText((String) editTableModel.getValueAt(selectedRow, 0));
        definitionInput.setText((String) editTableModel.getValueAt(selectedRow, 1));

        oldSlang = (String) editTableModel.getValueAt(selectedRow, 0);
        oldDefinition = (String) editTableModel.getValueAt(selectedRow, 1);
    }

    private JPanel miniGamePanel(){
        int size_text = 20;
        String font = Font.MONOSPACED;

        JLabel panel_label = new JLabel("MINI GAME");
        panel_label.setFont(new Font(font, Font.PLAIN, 40));

        slangOfTheDayButton = new JButton("Random Slang");
        slangOfTheDayButton.setFont(new Font(font, Font.BOLD, size_text));
        slangOfTheDayButton.setPreferredSize(new Dimension(200,35));
        slangOfTheDayButton.setFocusable(false);
        slangOfTheDayButton.addActionListener(this);

        miniGame1Button = new JButton("Mini Game 1");
        miniGame1Button.setFont(new Font(font, Font.BOLD, size_text));
        miniGame1Button.setPreferredSize(new Dimension(200,35));
        miniGame1Button.setFocusable(false);
        miniGame1Button.addActionListener(this);

        miniGame2Button = new JButton("Mini game 2");
        miniGame2Button.setFont(new Font(font, Font.BOLD, size_text));
        miniGame2Button.setPreferredSize(new Dimension(200,35));
        miniGame2Button.setFocusable(false);
        miniGame2Button.addActionListener(this);

        slangOfTheDayPanel = slangOfTheDay();
        miniGame1Panel = miniGame1Panel();
        miniGame2Panel = miniGame2Panel();

        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        button.add(slangOfTheDayButton);
        button.add(miniGame1Button);
        button.add(miniGame2Button);

        JPanel workPanel = new JPanel();
        workPanel.add(slangOfTheDayPanel);
        workPanel.add(miniGame1Panel);
        workPanel.add(miniGame2Panel);

        slangOfTheDayPanel.setVisible(false);
        miniGame1Panel.setVisible(false);
        miniGame2Panel.setVisible(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        panel.add(new JPanel().add(panel_label), gbc);
        gbc.insets = new Insets(40,0,20,0);
        gbc.gridy=1;
        panel.add(button, gbc);
        gbc.gridy=2;
        panel.add(workPanel, gbc);

        return panel;
    }

    private JPanel slangOfTheDay(){
        int size_text = 30;
        String font = Font.MONOSPACED;

        //---------------------- Initial -----------------------------\\
        JLabel panel_label = new JLabel("Slang of the day");
        panel_label.setFont(new Font(font, Font.PLAIN, 35));

        JLabel slang_label = new JLabel("Slang Word: ");
        slang_label.setFont(new Font(font, Font.PLAIN, size_text));

        JLabel slang_text = new JLabel();
        slang_text.setFont(new Font(font, Font.PLAIN, size_text));

        JLabel definition_label = new JLabel("Meaning: ");
        definition_label.setFont(new Font(font, Font.PLAIN, size_text));

        JTextArea definition_text = new JTextArea();
        definition_text.setFont(new Font(font, Font.PLAIN, size_text));
        definition_text.setLineWrap(true);
        definition_text.setWrapStyleWord(true);
        definition_text.setPreferredSize(new Dimension(550, 250));


        //---------------------- Random a slang -----------------------------\\
        int randomNumber = new Random().nextInt(slangListModel.size()) + 1;
        String slang = slangListModel.getElementAt(randomNumber).toString();
        List<String> values = dictionary.get(slang);
        String definition = "";
        for (int i = 0; i < values.size(); i++) {
            definition += values.get(i);
            definition += i == (values.size() - 1) ? "" : " | ";
        }

        slang_text.setText(slang);
        definition_text.setText(definition);

        //---------------------- Refresh button to random a slang -----------------------------\\
        JButton refresh_btn = new JButton("Refresh");
        refresh_btn.setFont(new Font(font, Font.BOLD, 20));
        refresh_btn.setPreferredSize(new Dimension(150,35));
        refresh_btn.setFocusable(false);
        refresh_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int randomNumber = new Random().nextInt(slangListModel.size()) + 1;
                String slang = slangListModel.getElementAt(randomNumber).toString();
                List<String> values = dictionary.get(slang);
                String definition = "";
                for (int i = 0; i < values.size(); i++) {
                    definition += values.get(i);
                    definition += i == (values.size() - 1) ? "" : " | ";
                }

                slang_text.setText(slang);
                definition_text.setText(definition);
            }
        });

        //---------------------- Layout setup -----------------------------\\
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,40,10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(panel_label, gbc);

        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(slang_label, gbc);
        gbc.gridx = 1;
        panel.add(slang_text, gbc);
        gbc.gridy = 2;
        panel.add(definition_text, gbc);
        gbc.gridx = 0;
        panel.add(definition_label, gbc);

        gbc.insets = new Insets(30,0,0,0);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(refresh_btn, gbc);

        return panel;
    }

    private JPanel miniGame1Panel(){
        int size_text = 25;
        String font = Font.MONOSPACED;

        //--------------------------- Initial ----------------------------------\\
        JLabel panel_label = new JLabel("Mini game 1");
        panel_label.setFont(new Font(font, Font.PLAIN, 30));

        JLabel request_label = new JLabel("Choose one correct definition of");
        request_label.setFont(new Font(font, Font.PLAIN, size_text));

        JLabel question_label = new JLabel();
        question_label.setFont(new Font(font, Font.PLAIN, size_text));
        question_label.setForeground(new Color(15, 175, 15));

        JRadioButton ans1 = new JRadioButton ();
        JRadioButton ans2 = new JRadioButton ();
        JRadioButton ans3 = new JRadioButton ();
        JRadioButton ans4 = new JRadioButton ();

        ButtonGroup group = new ButtonGroup();
        group.add(ans1);
        group.add(ans2);
        group.add(ans3);
        group.add(ans4);

        //---------------------------- Choose 4 random slang - definition -----------------------------\\
        List<String> slangList = new ArrayList<>();
        int i = 0;
        while(i < group.getButtonCount()){
            int randomNumber = new Random().nextInt(slangListModel.size());
            String slang = slangListModel.getElementAt(randomNumber).toString();
            if(!slangList.contains(slang)){
                slangList.add(slang);
                i++;
            } else{
                i--;
            }
        }

        List<JRadioButton> listCheck = new ArrayList<>();
        listCheck.add(ans1);
        listCheck.add(ans2);
        listCheck.add(ans3);
        listCheck.add(ans4);

        for(int j=0; j<listCheck.size(); j++){
            List<String> def = dictionary.get(slangList.get(j));
            String value = def.get(new Random().nextInt(def.size()));
            listCheck.get(j).setText(value);
            listCheck.get(j).setFont(new Font(font, Font.PLAIN, size_text));
            listCheck.get(j).setFocusable(false);
        }

        answerOfMiniGame1 = new Random().nextInt(slangList.size());
        question_label.setText(slangList.get(answerOfMiniGame1));

        //-------------------------- Submit answer and check -----------------------------\\
        JButton submit = new JButton("SUBMIT");
        submit.setFont(new Font(font, Font.PLAIN, size_text - 5));
        submit.setFocusable(false);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = -1;

                if(ans1.isSelected()){
                    answer = 0;
                }
                else if(ans2.isSelected()){
                    answer = 1;
                }
                else if(ans3.isSelected()){
                    answer = 2;
                }
                else if(ans4.isSelected()){
                    answer = 3;
                }

                if(answer != -1){
                    List<String> values = dictionary.get(slangList.get(answerOfMiniGame1));
                    String definition = "";
                    for (int i = 0; i < values.size(); i++) {
                        definition += values.get(i);
                        definition += i == (values.size() - 1) ? "" : " | ";
                    }

                    String str = "Slang word: " + slangList.get(answerOfMiniGame1) + "\nDefinition: " + definition;

                    if(answer == answerOfMiniGame1){
                        JOptionPane.showMessageDialog(miniGamePanel, "Correct Answer!\n" + str,
                                "^_^", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(miniGamePanel, "Wrong Answer!\n" + str,
                                "T_T", JOptionPane.INFORMATION_MESSAGE);
                    }

                    slangList.clear();
                    int i = 0;
                    while(i < group.getButtonCount()){
                        int randomNumber = new Random().nextInt(slangListModel.size());
                        String slang = slangListModel.getElementAt(randomNumber).toString();
                        if(!slangList.contains(slang)){
                            slangList.add(slang);
                            i++;
                        } else{
                            i--;
                        }
                    }

                    for(int j=0; j<listCheck.size(); j++){
                        List<String> def = dictionary.get(slangList.get(j));
                        String value = def.get(new Random().nextInt(def.size()));
                        listCheck.get(j).setText(value);
                    }

                    answerOfMiniGame1 = new Random().nextInt(slangList.size());
                    question_label.setText(slangList.get(answerOfMiniGame1));
                    group.clearSelection();
                }
                else{
                    JOptionPane.showMessageDialog(miniGamePanel, "Please choose an answer !",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        //------------------------------ Layout setup ---------------------------------\\
        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER, 30,0));
        button.add(submit);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,40,10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(panel_label, gbc);

        gbc.insets = new Insets(10,10,10,10);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(request_label, gbc);
        gbc.gridy = 2;
        panel.add(question_label, gbc);
        gbc.gridy = 7;
        panel.add(button, gbc);

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridy = 3;
        panel.add(ans1, gbc);
        gbc.gridy = 4;
        panel.add(ans2, gbc);
        gbc.gridy = 5;
        panel.add(ans3, gbc);
        gbc.gridy = 6;
        panel.add(ans4, gbc);

        return panel;
    }

    private JPanel miniGame2Panel(){
        int size_text = 25;
        String font = Font.MONOSPACED;

        //--------------------------- Initial ----------------------------------\\
        JLabel panel_label = new JLabel("Mini game 2");
        panel_label.setFont(new Font(font, Font.PLAIN, 30));

        JLabel request_label = new JLabel("Choose one correct slang word of definition");
        request_label.setFont(new Font(font, Font.PLAIN, size_text));

        JLabel question_label = new JLabel();
        question_label.setFont(new Font(font, Font.PLAIN, size_text));
        question_label.setForeground(new Color(15, 175, 15));

        JRadioButton ans1 = new JRadioButton ();
        JRadioButton ans2 = new JRadioButton ();
        JRadioButton ans3 = new JRadioButton ();
        JRadioButton ans4 = new JRadioButton ();

        ButtonGroup group = new ButtonGroup();
        group.add(ans1);
        group.add(ans2);
        group.add(ans3);
        group.add(ans4);

        //---------------------------- Choose 4 random slang - definition -----------------------------\\
        List<String> slangList = new ArrayList<>();
        int i = 0;
        while(i < group.getButtonCount()){
            int randomNumber = new Random().nextInt(slangListModel.size());
            String slang = slangListModel.getElementAt(randomNumber).toString();
            if(!slangList.contains(slang)){
                slangList.add(slang);
                i++;
            } else{
                i--;
            }
        }

        List<JRadioButton> listAnswer = new ArrayList<>();
        listAnswer.add(ans1);
        listAnswer.add(ans2);
        listAnswer.add(ans3);
        listAnswer.add(ans4);

        for(int j=0; j<listAnswer.size(); j++){
            listAnswer.get(j).setText(slangList.get(j));
            listAnswer.get(j).setFont(new Font(font, Font.PLAIN, size_text));
            listAnswer.get(j).setFocusable(false);
        }

        answerOfMiniGame2 = new Random().nextInt(slangList.size());
        List<String> def = dictionary.get(slangList.get(answerOfMiniGame2));
        String value = def.get(new Random().nextInt(def.size()));
        question_label.setText(value);

        //-------------------------- Submit answer and check -----------------------------\\
        JButton submit = new JButton("SUBMIT");
        submit.setFont(new Font(font, Font.PLAIN, size_text - 5));
        submit.setFocusable(false);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = -1;

                if(ans1.isSelected()){
                    answer = 0;
                }
                else if(ans2.isSelected()){
                    answer = 1;
                }
                else if(ans3.isSelected()){
                    answer = 2;
                }
                else if(ans4.isSelected()){
                    answer = 3;
                }

                if(answer != -1){
                    List<String> values = dictionary.get(slangList.get(answerOfMiniGame2));
                    String definition = "";
                    for (int i = 0; i < values.size(); i++) {
                        definition += values.get(i);
                        definition += i == (values.size() - 1) ? "" : " | ";
                    }

                    String str = "Slang word: " + slangList.get(answerOfMiniGame2) + "\nDefinition: " + definition;

                    if(answer == answerOfMiniGame2){
                        JOptionPane.showMessageDialog(miniGamePanel, "Correct Answer!\n" + str,
                                "^_^", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(miniGamePanel, "Wrong Answer!\n" + str,
                                "T_T", JOptionPane.INFORMATION_MESSAGE);
                    }
                    slangList.clear();
                    int i = 0;
                    while(i < group.getButtonCount()){
                        int randomNumber = new Random().nextInt(slangListModel.size());
                        String slang = slangListModel.getElementAt(randomNumber).toString();
                        if(!slangList.contains(slang)){
                            slangList.add(slang);
                            i++;
                        } else{
                            i--;
                        }
                    }

                    for(int j=0; j<listAnswer.size(); j++){
                        listAnswer.get(j).setText(slangList.get(j));
                    }

                    answerOfMiniGame2 = new Random().nextInt(slangList.size());
                    List<String> def = dictionary.get(slangList.get(answerOfMiniGame2));
                    String value = def.get(new Random().nextInt(def.size()));
                    question_label.setText(value);
                    group.clearSelection();
                }
                else{
                    JOptionPane.showMessageDialog(miniGamePanel, "Please choose an answer !",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        //------------------------------ Layout setup ---------------------------------\\
        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER, 30,0));
        button.add(submit);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,40,10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(panel_label, gbc);

        gbc.insets = new Insets(10,10,10,10);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(request_label, gbc);
        gbc.gridy = 2;
        panel.add(question_label, gbc);
        gbc.gridy = 7;
        panel.add(button, gbc);

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridy = 3;
        panel.add(ans1, gbc);
        gbc.gridy = 4;
        panel.add(ans2, gbc);
        gbc.gridy = 5;
        panel.add(ans3, gbc);
        gbc.gridy = 6;
        panel.add(ans4, gbc);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e)  {
        Object source = e.getSource();

        if(source.equals(slangSearchButton)){
            HashSet<String> keySet = new HashSet<>(dictionary.keySet());
            List<String> listResult = searchBySlang(searchInput.getText(), slangStore);
            definition.setText("");
            slangListModel.clear();
            slangListModel.addAll(listResult);
        }
        else if(source.equals(definitionSearchButton)){
            List<String> listResult = searchByDefinition(searchInput.getText(), defMap);

            definition.setText("");

            slangListModel.clear();
            slangListModel.addAll(listResult);
        }
        else if(source.equals(deleteHistoryButton)){
            historyListModel.clear();
            saveHistorySearch(historyFile, "", false);
        }
        else if(source.equals(addButton)){
            String slang = slangInput.getText();
            String definition = definitionInput.getText();

            boolean done = false;
            if("".equals(slang) || "".equals(definition)){
                JOptionPane.showMessageDialog(editPanel,
                        "Please input slang and definition !", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            else if(dictionary.containsKey(slang)){
                boolean validDefinition = true;

                for(String value : dictionary.get(slang)){
                    List<String> split = List.of(definition.split("\\|"));
                    if (split.contains(value)) {
                        validDefinition = false;
                        break;
                    }
                }
                if(!validDefinition) {
                    JOptionPane.showMessageDialog(editPanel,
                            "Slang and definition is exist! Invalid ADD action !", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
                else {
                    int choose = JOptionPane.showConfirmDialog(editPanel,
                            "Slang is exist! Do you want to overwrite all existing slang ?", "Warning",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (choose == JOptionPane.YES_OPTION) {
                        overwriteAllSlang(slangCloneFile, slang, definition);
                        updateTable(slang, definition, "overwrite");
                        updateMap(slang, definition, "", "overwrite");

                        done = true;
                    } else {
                        choose = JOptionPane.showConfirmDialog(editPanel,
                                "Do you want to duplicate this slang ?", "Warning",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (choose == JOptionPane.YES_OPTION) {
                            addNewSlang(slangCloneFile, slang, definition);
                            updateTable(slang, definition, "add");
                            updateMap(slang, definition, "","duplicate");

                            done = true;
                        }
                    }
                }
            }
            else {
                addNewSlang(slangCloneFile, slang, definition);
                updateTable(slang, definition, "add");
                updateMap(slang, definition, "", "add");

                done = true;
            }

            if(done){
                slangInput.setText("");
                definitionInput.setText("");
                JOptionPane.showMessageDialog(editPanel,
                        "Add slang word success !", "Notification", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if(source.equals(updateButton)){
            if(selectedRow <= editTableModel.getRowCount() && selectedRow >= 0 &&
            !"".equals(oldSlang) && !"".equals(oldDefinition)) {
                String newSlang = slangInput.getText();
                String newDefinition = definitionInput.getText();

                if (!oldSlang.equals(newSlang)) {
                    JOptionPane.showMessageDialog(editPanel,
                            "Different slang word is not accepted, please hold the old slang name, " +
                                    "update function is just modify definition!", "Notification",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    updateSlang(slangCloneFile, oldSlang, oldDefinition, newDefinition);
                    updateTable(oldSlang, newDefinition, "update");
                    updateMap(oldSlang, oldDefinition, newDefinition,"update");

                    JOptionPane.showMessageDialog(editPanel,
                            "Update success !", "Notification", JOptionPane.INFORMATION_MESSAGE);

                    slangInput.setText("");
                    definitionInput.setText("");

                }
            } else {
                JOptionPane.showMessageDialog(editPanel,
                        "Please choose correctly slang you want to update ?", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        else if(source.equals(deleteButton)){
            if(selectedRow <= editTableModel.getRowCount() && selectedRow >= 0 &&
                    !"".equals(slangInput.getText()) && !"".equals(definitionInput.getText())) {
                String slang = (String) editTableModel.getValueAt(selectedRow, 0);
                String definition = (String) editTableModel.getValueAt(selectedRow, 1);

                int choose = JOptionPane.showConfirmDialog(editPanel,
                        "Do you want to delete this slang ?", "Warning",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if(choose == JOptionPane.YES_OPTION){
                    deleteSlang(slangCloneFile, slang, definition);
                    updateTable(oldSlang, definition, "delete");
                    updateMap(slang, definition, "","delete");

                    JOptionPane.showMessageDialog(editPanel,
                            "Delete done !", "Notification", JOptionPane.INFORMATION_MESSAGE);

                    slangInput.setText("");
                    definitionInput.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(editPanel,
                        "Please choose correctly slang you want to delete ?", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        else if(source.equals(resetButton)){
            int choose = JOptionPane.showConfirmDialog(editPanel,
                    "Do you want to reset to origin file ?", "Warning",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (choose == JOptionPane.YES_OPTION) {
                deleteCloneFile(slangCloneFile);
                fillDataToTable(slangOriginFile, slangCloneFile);
                dictionary = new HashMap<>();
                defMap = new HashMap<>();
                readCloneFile(slangOriginFile, slangCloneFile, dictionary, defMap);

                JOptionPane.showMessageDialog(editPanel,
                        "Reset done !", "Notification",
                        JOptionPane.INFORMATION_MESSAGE);

                slangInput.setText("");
                definitionInput.setText("");
            }
        }
        else if(source.equals(slangOfTheDayButton)){
            slangOfTheDayPanel.setVisible(true);
            miniGame1Panel.setVisible(false);
            miniGame2Panel.setVisible(false);
        }
        else if(source.equals(miniGame1Button)){
            slangOfTheDayPanel.setVisible(false);
            miniGame1Panel.setVisible(true);
            miniGame2Panel.setVisible(false);
        }
        else if(source.equals(miniGame2Button)){
            slangOfTheDayPanel.setVisible(false);
            miniGame1Panel.setVisible(false);
            miniGame2Panel.setVisible(true);
        }
    }
}
