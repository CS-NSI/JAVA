package fr.wildcraft.launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.colored.SColoredButton;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener {
	
	
	private Image background = Swinger.getResource("background.png");
	
	private Saver saver = new Saver(new File(Launcher.WC_DIR, "launcher.properties"));
	
	private JTextField usernameField = new JTextField(this.saver.get("username"));
	private JTextField passwordField = new JPasswordField();
	
	private STexturedButton playButton = new STexturedButton(Swinger.getResource("play.png"));
	private STexturedButton quitButton = new STexturedButton(Swinger.getResource("quit.png"));
	private STexturedButton hideButton = new STexturedButton(Swinger.getResource("hide.png"));
	
	private SColoredButton ramButton = new SColoredButton(Swinger.getTransparentWhite(100), Swinger.getTransparentWhite(175));
	
	private SColoredBar progressBar = new SColoredBar(Swinger.getTransparentWhite(100), Swinger.getTransparentWhite(175));
	private JLabel infoLabel = new JLabel("Clique Sur Jouer!", SwingConstants.CENTER);
	
	private RamSelector ramSelector = new RamSelector(new File(Launcher.WC_DIR, "ram.txt"));
	
	
	
	public LauncherPanel() {
		this.setLayout(null);
		
		usernameField.setForeground(Color.WHITE);
		usernameField.setFont(usernameField.getFont().deriveFont(20F));
		usernameField.setCaretColor(Color.WHITE);
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setBounds(370, 312, 263, 44);
		this.add(usernameField);
		
		passwordField.setForeground(Color.WHITE);
		passwordField.setFont(usernameField.getFont().deriveFont(20F));
		passwordField.setCaretColor(Color.WHITE);
		passwordField.setOpaque(false);
		passwordField.setBorder(null);
		passwordField.setBounds(370, 415, 263, 44);
		this.add(passwordField);
		
		playButton.setBounds(363, 481);
		playButton.addEventListener(this);
		this.add(playButton);
		
		quitButton.setBounds(912, 35);
		quitButton.addEventListener(this);
		this.add(quitButton);
		
		hideButton.setBounds(885, 35);
		hideButton.addEventListener(this);
		this.add(hideButton);
		
		progressBar.setBounds(25, 580, 925, 20);
		this.add(progressBar);
		
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(usernameField.getFont());
		infoLabel.setBounds(25, 555, 925, 20);
		this.add(infoLabel);
		
		this.ramButton.addEventListener(this);
		this.ramButton.setBounds(855, 35, 19, 19);
		this.add(ramButton);
	}
	







	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage (background, 0, 0, this.getWidth(), this.getHeight(), this);
		
		
	}


	@Override
	public void onEvent(SwingerEvent event) {
		if(event.getSource() == playButton) {
			setFieldEnabled(false);
	
			
			if(usernameField.getText().replaceAll(" ", "").length() == 0 || passwordField.getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un pseudo et un mot de passe valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
				setFieldEnabled(true);
				return;
			}
			
			
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Launcher.auth(usernameField.getText(), passwordField.getText());
					} catch (AuthenticationException e) {
						LauncherFrame.getCrashReporter().catchError(e, "Erreur, impossible de se connecter");
						setFieldEnabled(true);
						return;	
					}
					
					saver.set("username",usernameField.getText());
					try {
						Launcher.update();
					} catch (Exception e) {
						Launcher.interruptThread();
						LauncherFrame.getCrashReporter().catchError(e, "Erreur, impossible de mettre WildCraft à jour"); 
					}
					try {
						Launcher.launch();
					} catch (LaunchException e) {
						LauncherFrame.getCrashReporter().catchError(e, "Erreur, impossible de lancer le jeu"); 
						setFieldEnabled(true);
					}
				}
			};
			t.start();
	} else if(event.getSource() == quitButton)
		System.exit(0);
	else if(event.getSource() == hideButton)
		LauncherFrame.getInstance().setState(1);	
	else if (event.getSource() == this.ramButton)
		ramSelector.display();
	}
	private void setFieldEnabled(boolean enabled) {
		usernameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);
		playButton.setEnabled(enabled);
	}
	
	public SColoredBar getProgressBar() {
		return progressBar;
	}
	
	public void setInfoText(String text) {
		infoLabel.setText(text);
	}
	
	public RamSelector getRamSelector()
	{
		return ramSelector;
	}

}
