import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;

/**
 * Original GUI design
 * @author 13141188
 *
 */
public class BasicForm{
	private static DefaultListModel<String> listModel = new DefaultListModel<String>();
	private static JList<String> dataList = new JList<String>(listModel);

	protected static void createAndShowGUI(){
		JFrame frame = new JFrame("Appointment Book");
		frame.setSize(600,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//MenuBar (Top Toolbar)
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu file = new JMenu("File");
		menuBar.add(file);
		//Menu Items for File
		JMenuItem importFile = new JMenuItem("Import");
		JMenuItem exportFile = new JMenuItem("Export");
		JMenuItem exit = new JMenuItem("Exit");

		//adding file menuItems and actionListeners
		file.add(importFile);
		class importAction implements ActionListener{
			public void actionPerformed(ActionEvent e){

			}
		}
		file.add(exportFile);
		//exit button add/action
		file.add(exit);
		class exitAction implements ActionListener{
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		}
		exit.addActionListener(new exitAction());

		JMenu help = new JMenu("Help");
		menuBar.add(help);
		//Menu Items for help
		JMenuItem about = new JMenuItem("About");
		help.add(about);

		JPanel panel = new JPanel();
		frame.add(panel);
		placeComponents(panel);

		frame.setVisible(true);
	}
/**
 * placing Jpanels to GUI and inputting methods
 * @param panel
 */
	public static void placeComponents(JPanel panel) {

		panel.setLayout(null);		
		
		//date label and text field
		JLabel dateLabel = new JLabel("Date");
		dateLabel.setBounds(10, 10, 80, 25);
		panel.add(dateLabel);
		
		DateFormat dayFormat = new SimpleDateFormat("dd");
		final JFormattedTextField dayTextField = new JFormattedTextField(dayFormat);
		dayTextField.setBounds(50,10,25,25);
		panel.add(dayTextField);

		DateFormat monthFormat = new SimpleDateFormat("MM");
		final JFormattedTextField monthTextField = new JFormattedTextField(monthFormat);
		monthTextField.setBounds(110,10,25,25);
		panel.add(monthTextField);

		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		final JFormattedTextField yearTextField = new JFormattedTextField(yearFormat);
		yearTextField.setBounds(170,10,50,25);
		panel.add(yearTextField);

		JLabel startTime = new JLabel("Start Time");
		startTime.setBounds(265, 10, 80, 25);
		panel.add(startTime);

		final JTextField startHrsTimeInput = new JTextField(4);
		startHrsTimeInput.setBounds(340, 10, 25, 25);
		panel.add(startHrsTimeInput);

		final JTextField startMinsTimeInput = new JTextField(2);
		startMinsTimeInput.setBounds(370, 10, 25, 25);
		panel.add(startMinsTimeInput);

		JLabel endTime = new JLabel("End Time");
		endTime.setBounds(265, 40, 80, 25);
		panel.add(endTime);

		final JTextField endHrsTimeInput = new JTextField(4);
		endHrsTimeInput.setBounds(340, 40, 25, 25);
		panel.add(endHrsTimeInput);

		final JTextField endMinsTimeInput = new JTextField(2);
		endMinsTimeInput.setBounds(370, 40, 25, 25);
		panel.add(endMinsTimeInput);

		// appointment title text field and label
		JLabel titleLabel = new JLabel("Title");
		titleLabel.setBounds(10, 40, 80, 25);
		panel.add(titleLabel);

		final JTextField titleText = new JTextField(20);
		titleText.setBounds(50, 40, 170, 25);
		panel.add(titleText);
		
		final JList<String> dataList = new JList(listModel);
		JScrollPane pane = new JScrollPane(dataList);
		pane.setBounds(10,150,500,100);
		panel.add(pane);
		

		for (int i = 0; i < dataList.getModel().getSize(); i++){
			System.out.println(dataList.getModel().getElementAt(i));
		}

		//Add Remove and Show Buttons / actions
		JButton addButton = new JButton("Add Appointment");
		addButton.setBounds(10, 80, 160, 25);
		panel.add(addButton);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//once input -1 off month according to gregorianCalendar

				//needs to know what is being added
				int year = Integer.parseInt(yearTextField.getText());
				int month = Integer.parseInt(monthTextField.getText()) - 1;
				int day = Integer.parseInt(dayTextField.getText());

				int startTimeMin = Integer.parseInt(startMinsTimeInput.getText());
				int startTimeHrs = Integer.parseInt(startHrsTimeInput.getText());

				int endTimeMin = Integer.parseInt(endMinsTimeInput.getText());
				int endTimeHrs = Integer.parseInt(endHrsTimeInput.getText());
				GregorianCalendar startDate = new GregorianCalendar(year, month, day, startTimeHrs, startTimeMin);
				GregorianCalendar endDate = new GregorianCalendar(year, month, day, endTimeHrs, endTimeMin);

				Appointment appointment = new Appointment(startDate, endDate, titleText.getText());
				errorCheckAppointment(appointment);
				Controller.addAppointment(appointment);
				//update list model from appBook
				updateJList();
			}
		});

		JButton removeButton = new JButton("Remove Appointment");
		removeButton.setBounds(180, 80, 160, 25);
		panel.add(removeButton);
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//needs to know what to remove
				Controller.removeAppointment(dataList.getSelectedIndex());
				updateJList();
			}
		});

		
		JButton showAllButton = new JButton("Show Appointments");
		showAllButton.setBounds(350, 80, 160, 25);
		panel.add(showAllButton);
		showAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateJList();
				Export.exportToText();
				Export.exportToICS();
				Export.exportToCSV();
			}
		});

		//search text field
		JTextField searchText = new JTextField();
		searchText.setBounds(350, 110, 70, 25);
		panel.add(searchText);
		//search button
		JButton searchButton = new JButton("Search");
		searchButton.setBounds(430, 110, 80, 25);
		panel.add(searchButton);
		//once performed should go through all appointments and check the text in the search box
		//against the eventTitles and update the jlist accordingly
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listModel.removeAllElements();
				for(Appointment appointment : Controller.appBook.getAllAppointments()){
					if(searchText.getText().equals(appointment.getEventTitle())){
						listModel.addElement(appointment.toString());
					}
				}
			}
		});

	}

	/**
	 * refreshes list, removes all appointments from JList
	 * reads them with the new addition to the arrayList
	 */
	private static void updateJList(){
		listModel.removeAllElements();
		for(Appointment apt : Controller.appBook.getAllAppointments()){
			listModel.addElement(apt.toString());
		}
	}

	/**
	 * error conflict checking
	 * creating clones to remove times from dates to just compare dates
	 * put all appointments with same date into array and then compare only the same day appointments
	 * to check for dateTime conflicts
	 * @param newAppointment
	 */
	public static void errorCheckAppointment(Appointment newAppointment){

		GregorianCalendar newAppointmentClone = (GregorianCalendar)newAppointment.getStartDateTime().clone();
		GregorianCalendar oldAppointClone;

		ArrayList<Appointment> appointments = Controller.getAllAppointments();

		ArrayList<Appointment> dateAppointment = new ArrayList<Appointment>();

		newAppointmentClone.clear(Calendar.MILLISECOND);
		newAppointmentClone.clear(Calendar.SECOND);
		newAppointmentClone.clear(Calendar.MINUTE);
		newAppointmentClone.clear(Calendar.HOUR_OF_DAY);
		
		//checking for dates that are the same as the newappointment

		for(Appointment appointment : appointments){
			oldAppointClone = (GregorianCalendar) appointment.getStartDateTime().clone();
			oldAppointClone.clear(Calendar.MILLISECOND);
			oldAppointClone.clear(Calendar.SECOND);
			oldAppointClone.clear(Calendar.MINUTE);
			oldAppointClone.clear(Calendar.HOUR_OF_DAY);

			if(newAppointmentClone.equals(oldAppointClone)){
				dateAppointment.add(appointment);
			}
		}
		
		//checking against times
/*		for(Appointment appointment : dateAppointment){
			if(newAppointment.getStartDateTime() == appointment.getStartDateTime()){
				System.out.println("Error Conflict");
			} else if (newAppointment.getEndDateTime() == appointment.getEndDateTime()){
				System.out.println("Error Conflict");
			} else if (newAppointment.getStartDateTime() > appointment.getStartDateTime() && newAppointment.getStartDateTime() < appointment.getEndDateTime()){
				System.out.println("Error Conflict");
			}
		}*/
		for(Appointment appointment : dateAppointment){
			
		}

//		if newStart == start {Error};
//		if newEnd == end{Error};
//
//		if newStart > start && < end {Error};
//		if newStart < start && newend > start {Error}
//		if newStart > start && newEnd > End && newStart < end {Error};
	}
}


